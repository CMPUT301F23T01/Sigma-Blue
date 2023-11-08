package com.example.sigma_blue;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

public class TagManagerFragment extends DialogFragment {
    private TagList tagsData;
    private FragmentLauncher fragmentLauncher;
    private RecyclerView tagRecyclerView;
    public TagListAdapter tagListAdapter;



    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // TODO Look into multiple item selection for the tag manager.

        // Get the data from the bundle I guess.
        tagListAdapter = TagListAdapter.newInstance(getContext(), new TagList(new TagDB()));
        fragmentLauncher = FragmentLauncher.newInstance(this);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.tag_manager_fragment, container, false);
    }
}
