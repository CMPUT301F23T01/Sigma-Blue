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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        // Tags in tagsData are passed from the current item, if applicable.
        // The Account of the user that is signed in is also passed into the fragment.
        final AddEditActivity activity = (AddEditActivity) requireActivity();

        // Load the shared data
        globalContext = GlobalContext.getInstance();

        if (globalContext.getCurrentState().equals("tag_manager_fragment")) {
            // User is opening the tag manager fragment on an existing fragment.
            // Check tags already applied onto the item.
            for (Tag t: globalContext.getCurrentItem().getTags()) {
                t.setChecked(true);
            }

        } else if (globalContext.getCurrentState().equals("multi_select_tag_manager_fragment")){
            // Don't check anything
        } else {
            throw new VerifyException("bad state");
        }

        /* Link the adapter to the UI */
        globalContext.getTagList().setAdapter(TagListAdapter.newInstance((ArrayList<Tag>) globalContext.getTagList().getTags(), getContext()));
        tagsListView.setAdapter(globalContext.getTagList().getAdapter());
        globalContext.getTagList().startListening();

        updateTagListView();

        /* On click listeners */

        // Handle the checkbox, and the checked state for the user selecting an item
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                globalContext.getTagList().getTags().get(position).toggleChecked();
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

        // Direct user to go to the tag edit fragment
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalContext.newState("tag_edit_fragment");
                NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
            }
        });

        // Handle the user confirming the tag addition.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateTagListView();
                ArrayList<Tag> tagsConfirmed = new ArrayList<>();
                for (Tag t: globalContext.getTagList().getTags()) {
                    if (t.isChecked()) {
                        tagsConfirmed.add(t);
                    }
                    t.setChecked(false);
                }

                if (Objects.equals(globalContext.getCurrentState(), "multi_select_tag_manager_fragment"))
                {
                    for (Item i : globalContext.getHighlightedItems()) {
                        for (Tag t : tagsConfirmed) {
                            i.addTag(t);
                        }
                    }
                    globalContext.newState("view_list_activity");
                    activity.returnAndClose();
                }
                else
                {
                    globalContext.getCurrentItem().setTags(tagsConfirmed);
                    globalContext.newState("edit_item_fragment");
                    NavHostFragment.findNavController(TagManagerFragment.this).navigate(R.id.action_tagManagerFragment_to_editFragment);
                }

            }
        });

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
     * Updates the adapter, as well as the Edit Tag button when called. The user should
     * not be able to edit a tag if they have selected 0, or than 1 tags.
     */
    public void updateTagListView() {
        //globalContext.getTagList().getAdapter().notifyDataSetChanged();

        // Poll through each of the tags and check if they are checked
        int checkedTags = 0;
        for (Tag t: globalContext.getTagList().getTags()) {
            if (t.isChecked()) { checkedTags++; }
        }

        if (checkedTags >= 1) {
            tagEditButton.setEnabled(true);
        } else {
            tagEditButton.setEnabled(false);
        }
    }
}
