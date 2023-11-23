package com.example.sigma_blue.entity.tag;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.sigma_blue.R;
import com.example.sigma_blue.adapter.ASelectableListAdapter;
import com.example.sigma_blue.context.GlobalContext;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagListAdapter extends ASelectableListAdapter<Tag> {
    /**
     * Base factory that takes in a tagList object and returns the adapter for it
     * @param tagsData an ArrayList of Tags, containing the tags for each item.
     * @return a TagListAdapter object that has been instantiated.
     */
    public static TagListAdapter newInstance(List<Tag> tagsData, Context context) {
        return new TagListAdapter(tagsData, context);
    }

    /**
     * Basic constructor that takes in the list that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param entityData the ArrayList providing the entity data that we will display with this adapter.
     */
    public TagListAdapter(List<Tag> entityData, Context context) {
        super(entityData, context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Tag tag = entityData.get(position);

        GlobalContext globalContext = GlobalContext.getInstance();
        // Inflate the custom layout.
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.tag_row, parent, false);
        }

        // Obtain UI elements from custom layout and represent the items appropriately.
        TextView tagTitle = view.findViewById(R.id.tagName);
        View tagColor = view.findViewById(R.id.tagColor);
        //CheckBox tagCheckBox = view.findViewById(R.id.tagCheckBox);

        tagColor.setBackgroundColor(tag.getColour().toArgb());
        tagTitle.setText(tag.getTagText());
        //tagCheckBox.setChecked(globalContext.getHighlightedTags().contains(tag));

        // Highlight the tag if selected.
        highlightControl(view, globalContext.getSelectedTags().getSelected().contains(tag));
        return view;
    }
}
