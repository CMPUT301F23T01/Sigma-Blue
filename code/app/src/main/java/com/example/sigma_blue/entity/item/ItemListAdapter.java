package com.example.sigma_blue.entity.item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;


import com.example.sigma_blue.R;
import com.example.sigma_blue.adapter.ASelectableListAdapter;

import android.widget.BaseAdapter;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ItemListAdapter extends ASelectableListAdapter<Item> {
    /* The the lists that are relevant to the item list adapter */
    private TextView sumView;

    /**
     * The constructor of the adapter.
     * @param context is the activity context in which the adapter is binding to
     *                this adapter
     * @param sumView   is the summary text view that is displaying the total
     *                  value of the application.
     */
    public ItemListAdapter(List<Item> entityData, final Context context, final TextView sumView) {
        super(entityData, context);
        this.sumView = sumView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();   // Need the original to still run
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
            convertView = inflater.inflate(R.layout.view_row, parent, false);
        }
        bindPosition(convertView, position);
        // Highlight the tag if selected.
        highlightControl(convertView, globalContext.getSelectedItems().getSelected().contains(entityData.get(position)));

        return convertView;
    }

    /**
     * Returns the formatted output for the summary text view.
     * @param sum is a float that represents the sum
     * @return the formatted string.
     */
    public String formatSummary(Double sum) {
        return String.format(Locale.ENGLISH,
                "The total value: %7.2f", sum);
    }


    /**
     * Updates the text on the sumView that is contained in the adapter
     * @param sum is the optional object that will either be empty or contain
     *            the sum of the values of the held object.
     */
    public void notifySumView(Optional<Double> sum) {
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
        if (position > entityData.size()) throw new IllegalArgumentException();
        else {
            rowItem = entityData.get(position);   // Caching is less expensive
            ((TextView) view.findViewById(R.id.itemName)).setText(rowItem
                    .getName());
            ((TextView) view.findViewById(R.id.itemMake)).setText(rowItem
                    .getMake());
            ((TextView) view.findViewById(R.id.uniqueId)).setText(rowItem
                    .getFormattedValue());  // Showing value for now
        }
    }
}
