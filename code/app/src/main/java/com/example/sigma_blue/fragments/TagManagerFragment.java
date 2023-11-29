package com.example.sigma_blue.fragments;

import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.databinding.TagManagerFragmentBinding;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.tag.Tag;

import com.example.sigma_blue.entity.tag.TagList;

import com.example.sigma_blue.entity.tag.TagListAdapter;
import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.entity.item.Item;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.VerifyException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private Button tagDeleteButton;
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
        tagDeleteButton = binding.getRoot().findViewById(R.id.tagManageTagDeleteButton);
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

        /* Link the adapter to the UI */
        globalContext.getTagList().setAdapter(
                TagListAdapter.newInstance((ArrayList<Tag>) globalContext.getTagList().getEntityList(), getContext()));

        tagsListView.setAdapter(globalContext.getTagList().getAdapter());
        globalContext.getTagList().startListening();

        globalContext.getSelectedTags().resetSelected();

        if (globalContext.getCurrentState().equals(ApplicationState
                .TAG_MANAGER_FRAGMENT)) {
            // User is opening the tag manager fragment on an existing fragment.
            // Check tags already applied onto the item.
            for (Tag t: globalContext.getModifiedItem().getTags()) {
                globalContext.getSelectedTags().toggleHighlight(t);
            }
            globalContext.getTagList().getAdapter().notifyDataSetChanged();

        } else if (globalContext.getCurrentState() == (ApplicationState
                .MULTI_SELECT_TAG_MANAGER_FRAGMENT)){
            // Don't check anything
            Log.v("VERBOSE", "No checks on multiselect");
        } else {
            throw new VerifyException("bad state");
        }

        updateTagListView();

        /* On click listeners */

        // The user must long click each tag list to select tags.
        tagsListView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    final Tag tagCache = globalContext.getTagList()
                            .getEntityList().get(position);
                    this.handleClick(tagCache);

                    updateTagListView();

                });

        // Direct user to go to the tag add fragment
        tagCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_tagAddFragment);
            }
        });

        // Go back to either the list view or the edit item fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalContext.getCurrentState() == ApplicationState
                        .MULTI_SELECT_TAG_MANAGER_FRAGMENT) {
                    globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                    // Reset the selected items.
                    globalContext.getSelectedItems().resetSelected();
                    globalContext.getItemList().getAdapter().notifyDataSetChanged();
                    activity.returnAndClose();
                } else {
                    globalContext.newState(globalContext.getLastState());
                    NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
                }
            }
        });

        // Handle the user confirming the tag addition.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTagListView();
                updateItemsWithTags();
                if (globalContext.getCurrentState() ==
                        ApplicationState.MULTI_SELECT_TAG_MANAGER_FRAGMENT) {
                    globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                    globalContext.getSelectedTags().resetSelected();
                    globalContext.getSelectedItems().resetSelected();
                    globalContext.getTagList().getAdapter().notifyDataSetChanged();
                    globalContext.getItemList().getAdapter().notifyDataSetChanged();
                    activity.returnAndClose();
                }
                else {
                    globalContext.newState(globalContext.getLastState());
                    globalContext.getTagList().getAdapter().notifyDataSetChanged();
                    globalContext.getSelectedTags().resetSelected();
                    NavHostFragment.findNavController(
                            TagManagerFragment.this).navigate(R.id
                            .action_tagManagerFragment_to_editFragment);

                }
            }
        });

        tagEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {if (globalContext.getSelectedTags().size() == 1) {
                    NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_tagEditFragment);
                } else {
                    Snackbar incorrectMessage = Snackbar.make(v, "Select one tag to edit", Snackbar.LENGTH_LONG);
                    incorrectMessage.show();
                }
            }
        });

        tagDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setMessage("Please confirm the deletion of the selected tag(s).");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (Tag t : globalContext.getSelectedTags().getSelected()) {
                                    globalContext.getTagList().remove(t);
                                    globalContext.getSelectedTags().toggleHighlight(t);
                                    globalContext.getItemList().cleanAllItemTags(globalContext.getTagList().getList());
                                    globalContext.getModifiedItem().cleanTags(globalContext.getTagList().getList());
                                    // remove dead tag from items
                                }
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    /**
     * Update the UI to reflect new tag information
     */
    private boolean updateTagListView() {
        globalContext.getTagList().getAdapter().notifyDataSetChanged();
        int checkedTags = globalContext.getSelectedTags().size();
        if (checkedTags == 1) {
            tagEditButton.setEnabled(true);
            tagEditButton.setBackgroundColor(androidx.appcompat.R.attr.colorPrimary);
            return true;
        } else {
            tagEditButton.setEnabled(false);
            tagEditButton.setBackgroundColor(getResources().getColor(androidx.cardview.R.color.cardview_dark_background));
            return false;
        }
    }
    /**
     * Updates item(s) with the selected tags.
     */
    private void updateItemsWithTags() {
        if (globalContext.getCurrentState() == ApplicationState
                .MULTI_SELECT_TAG_MANAGER_FRAGMENT) {
            for (Item i : globalContext.getSelectedItems().getSelected()) {
                for (Tag t : globalContext.getSelectedTags().getSelected()) {
                    i.addTag(t);
                }
                globalContext.getItemList().syncEntity(i);
            }
        }
        else {
            globalContext.getModifiedItem().setTags(globalContext.getSelectedTags().getSelected());
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

    /**
     * Listened to deal with long presses.
     * @param tag Tag that was long pressed on
     */
    private void handleClick(Tag tag) {
        Log.i("DEBUG", tag.getTagText() + " pressed");
        globalContext.getSelectedTags().toggleHighlight(tag);
        globalContext.getTagList().getAdapter().notifyDataSetChanged();
    }
}
