package com.example.sigma_blue;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TagManagerFragment extends DialogFragment {
    private ArrayList<Tag> tagsData;
    private FragmentLauncher fragmentLauncher;
    private TagAddFragment tagAddFragment;
    private TagEditFragment tagEditFragment;
    public TagListAdapter tagListAdapter;

    // Fragment components
    Button tagCreateButton;
    Button tagEditButton;
    Button backButton;
    Button confirmButton;


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            tagsData = (ArrayList<Tag>) getArguments().getSerializable(EditFragment.ARG_TAGS);
        }
        // to go to a new fragment example:
        // NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_tagManagerFragment, bundle);

        fragmentLauncher = FragmentLauncher.newInstance(this);
        // TODO Look into multiple item selection for the tag manager.
        tagListAdapter = TagListAdapter.newInstance(getContext(), tagsData);

        /* On click listeners */
        tagCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = getView();

        // Linking the UI components.
        tagCreateButton = view.findViewById(R.id.tagManageCreateButton);
        tagEditButton = view.findViewById(R.id.tagManageEditButton);
        backButton = view.findViewById(R.id.tagManageBackButton);
        confirmButton = view.findViewById(R.id.tagManageConfirmButton);

        return inflater.inflate(R.layout.tag_manager_fragment, container, false);
    }
}
