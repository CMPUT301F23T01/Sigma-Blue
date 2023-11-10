package com.example.sigma_blue;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
    private ArrayList<Tag> selectedTags;

    /* Factories and Constructors */

    /**
     * Base factory that takes in a tagList object and returns the adapter for it
     * @param tagsData an ArrayList of Tags, containing the tags for each item.
     * @return a TagListAdapter object that has been instantiated.
     */
    public static TagListAdapter newInstance(ArrayList<Tag> tagsData, Context context) {
        return new TagListAdapter(tagsData, context);
    }

    /**
     * Basic constructor that takes in the TagList that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param tagsData the ArrayList providing the tag data that we will display with this adapter.
     */
    public TagListAdapter(ArrayList<Tag> tagsData, Context context) {
        super(context, 0, tagsData);
        this.context = context;
        this.tagsData = tagsData;
        this.selectedTags = new ArrayList<Tag>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Tag tag = tagsData.get(position);

        // Inflate the custom layout.
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.tag_row, parent, false);
        }

        // Obtain UI elements from custom layout and represent the items appropriately.
        TextView tagTitle = view.findViewById(R.id.tagName);
        View tagColor = view.findViewById(R.id.tagColor);
        CheckBox tagCheckBox = view.findViewById(R.id.tagCheckBox);

        tagColor.setBackgroundColor(tag.getColour().toArgb());
        tagTitle.setText(tag.getTagText());
        tagCheckBox.setChecked(tag.isChecked());

        return view;
    }

    @Override
    public int getCount() {
        return tagsData.size();
    }

}
