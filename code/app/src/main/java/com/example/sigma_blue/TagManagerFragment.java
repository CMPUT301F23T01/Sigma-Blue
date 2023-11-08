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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TagManagerFragment extends DialogFragment {
    private TagList tagsData;
    private FragmentLauncher fragmentLauncher;
    private RecyclerView tagRecyclerView;
    public TagListAdapter tagListAdapter;

    private TagListAdapter.RecyclerViewHolder viewHolder;


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        fragmentLauncher = FragmentLauncher.newInstance(this);

        // TODO Look into multiple item selection for the tag manager.

        TagDB dummyTagDB = new TagDB();
        tagsData = new TagList(dummyTagDB);
        Tag dt = new Tag("Graphics card", Color.parseColor("#ff0000"));
        tagsData.addTag(dt);
        Tag dt2 = new Tag("Graphics card", Color.parseColor("#ff0000"));
        tagsData.addTag(dt2);

        tagListAdapter = TagListAdapter.newInstance(getContext(), tagsData);

        tagRecyclerView = view.findViewById(R.id.tagManageRecyclerView);
        tagRecyclerView.setHasFixedSize(true);

        tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tagRecyclerView.setAdapter(tagListAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.tag_manager_fragment, container, false);
    }
}
