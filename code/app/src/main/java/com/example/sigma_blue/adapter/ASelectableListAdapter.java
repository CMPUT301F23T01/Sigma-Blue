package com.example.sigma_blue.adapter;

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
import com.example.sigma_blue.context.GlobalContext;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ASelectableListAdapter<T> extends ArrayAdapter<T> {

    /* Attributes */
    protected List<T> entityData;
    protected Context context;
    protected GlobalContext globalContext;
    /* Factories and Constructors */


    /**
     * Basic constructor that takes in the list that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param entityData the ArrayList providing the entity data that we will display with this adapter.
     */
    public ASelectableListAdapter(List<T> entityData, Context context) {
        super(context, 0, entityData);
        globalContext = GlobalContext.getInstance();
        this.context = context;
        this.entityData = entityData;
    }

    @Override
    public int getCount() {
        return entityData.size();
    }

    public void setList(List<T> entityData) {
        this.entityData = entityData;
    }


    /**
     * Method that will turn on the highlight of the view if it is selected,
     * otherwise, reset it to the default background color.
     * @param view is the view that is being checked.
     * @param selected
     */
    protected void highlightControl(View view, boolean selected) {
        @ColorInt int rowColor;
        if (selected) rowColor = ContextCompat.getColor(getContext(),
                R.color.add_edit_layout_bgr_test);
        else rowColor = ContextCompat.getColor(getContext(), R.color.white);
        view.setBackgroundColor(rowColor);
    }
}
