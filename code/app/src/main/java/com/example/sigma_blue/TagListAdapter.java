package com.example.sigma_blue;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TagListAdapter extends ArrayAdapter<Tag> {


    /* Attributes */
    private ArrayList<Tag> tagsData;
    private Context context;

    /* Factories and Constructors */

    /**
     * Base factory that takes in a tagList object and returns the adapter for it
     * @param tagsData an ArrayList of Tags, containing the tags for each item.
     * @return a TagListAdapter object that has been instantiated.
     */
    public static TagListAdapter newInstance(Context context, ArrayList<Tag> tagsData) {
        return new TagListAdapter(context, tagsData);
    }

    /**
     * Basic constructor that takes in the TagList that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param tagsData the ArrayList that we will display on lists through this adapter.
     */
    public TagListAdapter(Context context, ArrayList<Tag> tagsData) {
        super(context, 0, tagsData);
        this.context = context;
        this.tagsData = tagsData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.tag_row, parent, false);
        }

        Tag tag = tagsData.get(position);
        TextView tagTitle = view.findViewById(R.id.tagName);
        View tagColor = view.findViewById(R.id.tagColor);

        tagTitle.setText(tag.getTagText());
        tagColor.setBackgroundColor(Color.parseColor("#" + tag.getColour()));

        return view;
    }
}
