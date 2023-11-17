package com.example.sigma_blue.entity.item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;


import com.example.sigma_blue.R;

import android.widget.BaseAdapter;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ItemListAdapter extends BaseAdapter {
    /* The the lists that are relevant to the item list adapter */
    private List<? extends Item> itemList;      // All the items
    private List<? extends Item> selectedItems; // The list of selected items

    private final LayoutInflater inflater;
    private TextView sumView;
    private final Context context;

    /**
     * The constructor of the adapter.
     * @param context is the activity context in which the adapter is binding to
     *                this adapter
     * @param sumView   is the summary text view that is displaying the total
     *                  value of the application.
     */
    public ItemListAdapter(final Context context, final TextView sumView) {
        inflater = LayoutInflater.from(context);
        this.sumView = sumView;
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();   // Need the original to still run
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
     * The list of selected items. (Likely the same list as the one held by the
     * global context).
     * @param selectedList is the list of selected items. Assumption that it
     *                     will always be a subset of the itemList.
     */
    public void setSelectedItemList(List<? extends Item> selectedList) {
        this.selectedItems = selectedList;
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
     * @return the item stored at that location
     */
    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    /**
     * Returns the row ID, which is not the same as uniqueness ID.
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the integer that is
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
        if (convertView == null) convertView = getLayoutInflater()
                .inflate(R.layout.view_row, null);
        else ;  // Added reuse of the view.
        bindPosition(convertView, position);
        highlightControl(convertView, selectedItems.contains(itemList
                .get(position)));
        return convertView;
    }

    /**
     * layout inflater getter
     * @return return the layout inflater.
     */
    public LayoutInflater getLayoutInflater() {
        return this.inflater;
    }

    /**
     * Returns the formatted output for the summary text view.
     * @param sum is a float that represents the sum
     * @return the formatted string.
     */
    public String formatSummary(Float sum) {
        return String.format(Locale.ENGLISH,
                "The total value: %7.2f", sum);
    }


    /**
     * Updates the text on the sumView that is contained in the adapter
     * @param sum is the optional object that will either be empty or contain
     *            the sum of the values of the held object.
     */
    public void notifySumView(Optional<Float> sum) {
        if (this.sumView != null) {
            if (sum.isPresent()) this.sumView
                    .setText(formatSummary(sum.get()));
            else this.sumView
                    .setText(R.string.empty_summary_view);
        } else Log.w("Missing Summary View",
                "Summary view not hooked to adapter");
    }

    /**
     * Sets the summary view (TextView that contains the total value of items
     * held).
     * @param sumView is the TextView that will display the summation
     */
    public void setSummaryView(TextView sumView) {
        this.sumView = sumView;
    }

    /**
     * Method for binding the item at the specified location to the given view
     * @param view is the view being bound
     * @param position is the position that is being checked
     */
    private void bindPosition(View view, int position) {
        Item rowItem;
        if (position > itemList.size()) throw new IllegalArgumentException();
        else {
            rowItem = itemList.get(position);   // Caching is less expensive
            ((TextView) view.findViewById(R.id.itemName)).setText(rowItem
                    .getName());
            ((TextView) view.findViewById(R.id.itemMake)).setText(rowItem
                    .getMake());
            ((TextView) view.findViewById(R.id.uniqueId)).setText(rowItem
                    .getSerialNumber());
        }
    }

    /**
     * Method that will turn on the highlight of the view if it is selected,
     * otherwise reset it to the default background colour.
     * @param view is the view that is being checked.
     */
    private void highlightControl(View view, boolean selected) {
        @ColorInt int rowColor;
        if (selected) rowColor = ContextCompat.getColor(getContext(),
                R.color.add_edit_layout_bgr_test);
        else rowColor = ContextCompat.getColor(getContext(), R.color.white);
        view.setBackgroundColor(rowColor);
    }
}
