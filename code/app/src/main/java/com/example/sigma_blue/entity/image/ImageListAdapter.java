package com.example.sigma_blue.entity.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ImageListAdapter extends ArrayAdapter<Bitmap> {
    protected List<Bitmap> entityData;
    protected Context context;


    /**
     * Basic constructor that takes in the list that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param entityData the ArrayList providing the entity data that we will display with this adapter.
     */
    public ImageListAdapter(List<Bitmap> entityData, Context context) {
        super(context, 0, entityData);
        this.context = context;
        this.entityData = entityData;
    }

    /**
     * Constructor that starts empty
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     */
    public ImageListAdapter(Context context) {
        super(context, 0, new ArrayList<Bitmap>());
        this.entityData = new ArrayList<Bitmap>();
        this.context = context;
    }
    @Override
    public int getCount() {
        return entityData.size();
    }

    public void setList(List<Bitmap> entityData) {
        this.entityData = entityData;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    /**
     * This method is for binding row to view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return return new row view
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // only inflate if needed
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.image_row, parent, false);
        }
        if (position > entityData.size()) {
            throw new IllegalArgumentException();
        }
        else {
            ((ImageView) convertView.findViewById(R.id.image_list_image)).setImageBitmap(entityData.get(position));
        }

        return convertView;
    }
}
