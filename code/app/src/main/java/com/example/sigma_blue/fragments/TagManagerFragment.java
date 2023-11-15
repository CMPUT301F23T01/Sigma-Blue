package com.example.sigma_blue.fragments;

import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.databinding.TagManagerFragmentBinding;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.tag.Tag;

import com.example.sigma_blue.entity.tag.TagList;

import com.example.sigma_blue.entity.tag.TagListAdapter;
import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.entity.item.Item;
import com.google.common.base.VerifyException;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.Objects;

public class TagManagerFragment extends Fragment {
    private GlobalContext globalContext;

    // Fragment binding
    private TagManagerFragmentBinding binding;

    // Fragment UI components
    private Button tagCreateButton;
    private Button tagEditButton;
    private Button backButton;
    private Button confirmButton;
    private ListView tagsListView;
    private AddEditActivity activity;

    public TagManagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method to inflate layout of the fragment, as well as bind UI components.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
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

    /**
     * Method to set the details of the selected Item, if applicable in the fragment,
     * and handle button interactions.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        activity = (AddEditActivity) requireActivity();

        // Load the shared data
        globalContext = GlobalContext.getInstance();
        globalContext.resetHighlightedTags(); // probably not needed, but just to be sure

        if (globalContext.getCurrentState().equals("tag_manager_fragment")) {
            // User is opening the tag manager fragment on an existing fragment.
            // Check tags already applied onto the item.
            for (Tag t: globalContext.getCurrentItem().getTags()) {
                globalContext.toggleHighlightTag(t);
            }

        } else if (globalContext.getCurrentState().equals("multi_select_tag_manager_fragment")){
            // Don't check anything
        } else {
            throw new VerifyException("bad state");
        }

        /* Link the adapter to the UI */
        globalContext.getTagList().setAdapter(TagListAdapter.newInstance((ArrayList<Tag>) globalContext.getTagList().getTags(), getContext()));
        tagsListView.setAdapter(globalContext.getTagList().getAdapter());
        //tagsListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        globalContext.getTagList().startListening();

        updateTagListView();

        /* On click listeners */

        // Handle the checkbox, and the checked state for the user selecting an item
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalContext.toggleHighlightTag(globalContext.getTagList().getTags().get(position));
                updateTagListView();
            }
        });

        // Direct user to go to the tag add fragment
        tagCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalContext.newState("tag_add_fragment");
                NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_tagAddFragment);
            }
        });

        // Go back to either the list view or the edit item fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(globalContext.getCurrentState(), "multi_select_tag_manager_fragment")) {
                    globalContext.newState("view_list_activity");
                    activity.returnAndClose();
                } else {
                    globalContext.newState("edit_item_fragment");
                    NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
                }
            }
        });

        // Handle the user confirming the tag addition.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateTagListView()) {
                    updateItemsWithTags();
                    if (Objects.equals(globalContext.getCurrentState(), "multi_select_tag_manager_fragment")) {
                        globalContext.newState("view_list_activity");
                        globalContext.resetHighlightedTags();
                        activity.returnAndClose();
                    }
                    else {
                        globalContext.newState("edit_item_fragment");
                        NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
                    }
                }
            }
        });

        tagEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalContext.getHighlightedTags().size() == 1) {
                    globalContext.newState("edit_tag_fragment");
                    NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_tagEditFragment);
                } else {
                    //TODO show a useful error
                }
            }
        });

    }

    /**
     * Update the UI to reflect new tag information
     */
    private boolean updateTagListView() {
        globalContext.getTagList().getAdapter().notifyDataSetChanged();
        int checkedTags = globalContext.getHighlightedTags().size();
        if (checkedTags >= 1) {
            tagEditButton.setEnabled(true);
            return true;
        } else {
            tagEditButton.setEnabled(false);
            return false;
        }
    }
    /**
     * Updates item(s) with the selected tags.
     */
    private void updateItemsWithTags() {
        // check each of the tags and check if they are checked

        if (Objects.equals(globalContext.getCurrentState(), "multi_select_tag_manager_fragment")) {

            for (Item i : globalContext.getSelectedItems()) {

                i.setTags(globalContext.getHighlightedTags());
                globalContext.getItemList().updateItem(i, i); // this works since changing the tags doesn't impact the 'id' of an item
            }
        }
        else {
            globalContext.getCurrentItem().setTags(globalContext.getHighlightedTags());
        }
    }

    /**
     * Method called when the fragment is no longer in use; unbinds all UI elements.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
