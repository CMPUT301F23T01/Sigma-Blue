package com.example.sigma_blue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ItemListAdapter extends BaseAdapter {
    /* Attributes */
    private ItemList itemList;
    private Context applicationContext;

    /* Factories and Constructors */

    public static ItemListAdapter newInstance(ItemList itemList, Context context) {
        ItemListAdapter ret = new ItemListAdapter(itemList);
        ret.setContext(context);
        return ret;
    }

    public static ItemListAdapter newInstance(ItemList itemList) {
        return new ItemListAdapter(itemList);
    }

    /**
     * Basic constructor. Takes in the ItemList that will be adapted to a list view.
     * @param itemList is the ItemList object that will be displayed on lists through this adapter.
     */
    public ItemListAdapter(ItemList itemList) {
        this.itemList = itemList;
    }

    /**
     * The amount of Items that are currently held by the adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return itemList.getCount();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return itemList.getItem(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;    // TODO: Want to have HashCode for each item. We need to think about how
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /* Need to display to recycler view. */
        return null;
    }

    /* Getters and Setters */
    public void setContext(Context context) {
        applicationContext = context;
    }

    public void setItemList(ItemList itemList) {
        this.itemList = itemList;
    }
    public void addItem(Item item) {
        this.itemList.add(item);
    }
    public void removeItem(int position) {
        this.itemList.remove(position);
    }
}