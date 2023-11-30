package com.example.sigma_blue.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;

import com.example.sigma_blue.R;
import com.example.sigma_blue.adapter.TabMode;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.databinding.DetailsFragItemTagsBinding;
import com.example.sigma_blue.databinding.EditFragItemTagsBinding;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.tag.TagListAdapter;

import java.util.ArrayList;

public class ItemTagsFragment extends Fragment
{
    private final GlobalContext globalContext = GlobalContext.getInstance();
    private final TabMode mode;
    private ViewBinding binding;

    private ListView tagListView;
    private TagListAdapter tagListAdapter;
    private Button addTag;

    public ItemTagsFragment(TabMode mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Change binding to correspond to current UI and update additional ui components where applicable
        if (mode.equals(TabMode.Edit)) {
            binding = EditFragItemTagsBinding.inflate(inflater, container, false);
            addTag = binding.getRoot().findViewById(R.id.button_tag);
            updateBinding(binding);
        } else if (mode.equals(TabMode.Details)) {
            binding = DetailsFragItemTagsBinding.inflate(inflater, container, false);
            updateBinding(binding);
        }
        else {
            throw new RuntimeException("Unhandled mode assigned to instance");
        }

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize local variables
        Item modifiedItem = globalContext.getModifiedItem();

        tagListAdapter = TagListAdapter.newInstance(modifiedItem.getTags(), getContext());
        tagListView.setAdapter(tagListAdapter);

        if (mode == TabMode.Edit)
        {
            addTag.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    globalContext.newState(ApplicationState.TAG_MANAGER_FRAGMENT);
                    Log.i("NEW STATE", ApplicationState.TAG_MANAGER_FRAGMENT
                            .toString());
                    NavHostFragment.findNavController(ItemTagsFragment.this).navigate(R.id.action_editFragment_to_tagManagerFragment);
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateTags();
    }

    /**
     * Updates tag list from global context
     */
    public void updateTags() {
        tagListAdapter.setList(globalContext.getModifiedItem().getTags());
        tagListAdapter.notifyDataSetChanged();
    }

    /**
     * Method for destroying fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * updates the ui elements to the current binding
     * @param binding of the current xml reference
     */
    public void updateBinding(ViewBinding binding) {
        tagListView = binding.getRoot().findViewById(R.id.list_tag);
    }
}
