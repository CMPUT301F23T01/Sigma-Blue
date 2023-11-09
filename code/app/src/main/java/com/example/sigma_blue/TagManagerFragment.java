package com.example.sigma_blue;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sigma_blue.databinding.TagManagerFragmentBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TagManagerFragment extends Fragment {
    private ArrayList<Tag> tagsData;
    private FragmentLauncher fragmentLauncher;
    private TagAddFragment tagAddFragment;
    private TagEditFragment tagEditFragment;
    public TagListAdapter tagListAdapter;

    // Fragment binding
    private TagManagerFragmentBinding binding;

    // Fragment UI components
    private Button tagCreateButton;
    private Button tagEditButton;
    private Button backButton;
    private Button confirmButton;
    private ListView tagsListView;

    public TagManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TagManagerFragmentBinding.inflate(inflater, container, false);

        // Bind the UI components
        tagCreateButton = binding.getRoot().findViewById(R.id.tagManageCreateButton);
        tagEditButton = binding.getRoot().findViewById(R.id.tagManageEditButton);
        backButton = binding.getRoot().findViewById(R.id.tagManageBackButton);
        confirmButton = binding.getRoot().findViewById(R.id.tagManageConfirmButton);
        tagsListView = binding.getRoot().findViewById(R.id.tagManagerListView);


        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Set tag details for the Bundle, if applicable
        if (getArguments() != null) {
            tagsData = (ArrayList<Tag>) getArguments().getSerializable(
                    EditFragment.ARG_TAGS
            );
        }

        tagsData.add(new Tag("Testo", Color.parseColor("#FF0000")));
        tagsData.add(new Tag("Testo2", Color.parseColor("#FF0000")));

        /* Link the adapter to the UI */
        tagListAdapter = TagListAdapter.newInstance(tagsData, getContext());
        tagsListView.setAdapter(tagListAdapter);
        tagsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);



        /* On click listeners */
        // Direct user to go to a new tag
        tagCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_tagAddFragment);
            }
        });

        // TODO Implement data passing through fragments!
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateTagListView() {
        tagListAdapter.notifyDataSetChanged();
    }
}
