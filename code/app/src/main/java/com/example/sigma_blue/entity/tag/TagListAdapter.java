package com.example.sigma_blue.entity.tag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;

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
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Tag tag = tagsData.get(position);

        GlobalContext globalContext = GlobalContext.getInstance();
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
        tagCheckBox.setChecked(globalContext.getHighlightedTags().contains(tag));
        return view;
    }

    @Override
    public int getCount() {
        return tagsData.size();
    }

    public void setList(ArrayList<Tag> tags) {
        this.tagsData = tags;
    }
}
