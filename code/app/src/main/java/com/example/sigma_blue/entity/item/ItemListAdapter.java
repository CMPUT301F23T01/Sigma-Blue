package com.example.sigma_blue.entity.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sigma_blue.R;

import java.util.List;

public class ItemListAdapter extends BaseAdapter {
    private List<? extends Item> itemList;
    private LayoutInflater inflater;

    public static ItemListAdapter newInstance(Context context) {
        return new ItemListAdapter(context);
    }

    public static ItemListAdapter newInstance(Context context,
                                              List<? extends Item> itemList) {
        return new ItemListAdapter(context, itemList);
    }

    private ItemListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    /**
     * Constructor with array input.
     * @param itemList List that is being adapted to the list view
     */
    private ItemListAdapter(Context context, List<? extends Item> itemList) {
        inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    /**
     * Sets itemList
     * @param itemList Some sort of list that contains a subclass of the item
     *                 class.
     */
    public void setItemList(List<? extends Item> itemList) {
        this.itemList = itemList;
    }

    /**
     * Gets the amount of items stored in the list being adapted
     * @return the int representation of number of items
     */
    @Override
    public int getCount() {
        return itemList.size();
    }

    /**
     * Returns the item
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    /**
     * Returns the row ID, which is not the same as uniqueness ID.
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the intger that is
     */
    @Override
    public long getItemId(int position) {
        return position;
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
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) view = this.inflater.inflate(R.layout.view_row,
                parent);
        else view = convertView;
        bindPosition(view, position);   // Binds the item at the position
        return view;
    }

    /**
     * Method for binding the item at the specified location to the given view
     * @param view is the view being bound
     * @param position is the position that is being checked
     */
    private void bindPosition(View view, int position) {
        if (position > itemList.size()) throw new IllegalArgumentException();
        else {
            ((TextView) view.findViewById(R.id.itemName)).setText(itemList
                    .get(position).getName());
            ((TextView) view.findViewById(R.id.itemMake)).setText(itemList
                    .get(position).getName());
            ((TextView) view.findViewById(R.id.uniqueId)).setText(itemList
                    .get(position).getName());
        }
    }
}
