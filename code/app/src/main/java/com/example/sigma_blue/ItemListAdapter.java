package com.example.sigma_blue;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Modified adapter class for the list of items.
 */
public class ItemListAdapter extends
        RecyclerView.Adapter<ItemListAdapter.RecyclerViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
    public interface OnLongClickListener {
        void onLongClick(Item item);
    }
    private static OnItemClickListener clickListener;
    private static OnLongClickListener longClickListener;
    private ArrayList<RecyclerViewHolder> recyclerViewHolderList;


    /* Caching the views in the adapter. */
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView make;
        TextView id;
        Boolean highlighted;
        Item item;

        /* Constructor that accepts the entire row */
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.itemName);
            make = itemView.findViewById(R.id.itemMake);
            id = itemView.findViewById(R.id.uniqueId);
        }

        /**
         * binds the provided listener to the specific item row. Since ItemList is the class used
         * in activities the listener gets passed in from there
         * @param item Item that will be displayed on a row
         * @param clickListener method to call on a click
         * @param longClickListener method to call on a long click
         */
        public void bind(final Item item, final OnItemClickListener clickListener, final OnLongClickListener longClickListener) {
            this.highlighted = itemView.isSelected();
            this.item = item;
            itemView.setBackgroundColor(itemView.isSelected() ? Color.CYAN : Color.WHITE);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    highlighted = !highlighted;
                    itemView.setSelected(!itemView.isSelected());
                    itemView.setBackgroundColor(itemView.isSelected() ? Color.CYAN : Color.WHITE);
                    longClickListener.onLongClick(item);
                    return true;
                }

            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    clickListener.onItemClick(item);
                }
            });
        }

        /**
         * 'un-highlight' this item
         */
        public void resetHighlight() {
            itemView.setSelected(false);
            itemView.setBackgroundColor(Color.WHITE);
            highlighted = false;
        }

        public boolean getHighlighted() {
            return this.highlighted;
        }

        public Item getItem() {
            return this.item;
        }
    }

    /* Attributes */
    private List<Item> items;

    /* Due to the usage of listeners when updating from the database, it is
    * much simpler to embed the summary view into the adapter */
    private TextView summaryView;

    /* Factories and Constructors */

    /**
     * Base factory. Takes in an ItemList object and returns the adapter for it.
     * @param itemList is an ItemList object that contains the types.
     * @return an ItemListAdapter object that has been instantiated.
     */
    public static ItemListAdapter newInstance(List<Item> itemList, OnItemClickListener itemClickListener, OnLongClickListener longClickListener) {
        return new ItemListAdapter(itemList, itemClickListener, longClickListener);
    }
    public static ItemListAdapter newInstance() {
        return new ItemListAdapter();
    }

    /**
     * Basic constructor. Takes in the ItemList that will be adapted to a list view.
     * @param items is the ItemList object that will be displayed on lists through this adapter.
     */
    public ItemListAdapter(List<Item> items, OnItemClickListener itemClickListener, OnLongClickListener longClickListener) {
        this.items = items;
        this.clickListener = itemClickListener;
        this.longClickListener = longClickListener;
    }

    public ItemListAdapter() {
        this.items = new ArrayList<>();
    }

    /**
     * Called when RecyclerView needs a new {@link RecyclerViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerViewHolder, int, List)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerViewHolder, int)
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.recyclerViewHolderList == null) {
            recyclerViewHolderList = new ArrayList<RecyclerViewHolder>();
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        /* Need to inflate the custom layout */
        View itemView = inflater.inflate(R.layout.view_row, parent,
                false);
        RecyclerViewHolder r = new RecyclerViewHolder(itemView);
        this.recyclerViewHolderList.add(r);
        return r;
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link RecyclerViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link RecyclerViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override {@link #onBindViewHolder(RecyclerViewHolder, int, List)} instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder,
                                 int position) {
        /* Caching the item that will be used to fill up the row */
        Item item = items.get(position);

        /* Assigning the content of the view holder */
        holder.name.setText(item.getName());
        holder.make.setText(item.getMake());
        holder.id.setText(String.valueOf(item.getValue()));
        holder.bind(item, this.clickListener, this.longClickListener);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        if (position < items.size()) return items.get(position)
                .getDocID().hashCode();
        else return -1;
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setList(List<Item> itemList) {
        this.items = itemList;
    }

    public void setSummaryView(TextView view) {
        this.summaryView = view;
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

    public void updateSumView(Optional<Float> sum) {
        if (this.summaryView != null) {
            if (sum.isPresent()) this.summaryView
                    .setText(formatSummary(sum.get()));
            else this.summaryView
                    .setText(R.string.empty_summary_view);
        } else Log.w("Missing Summary View",
                "Summary view not hooked to adapter");
    }

    public ArrayList<Item> getHighlightedItems() {
        ArrayList<Item> highlightedItems = new ArrayList<Item>();
        for (RecyclerViewHolder H : recyclerViewHolderList) {
            if (H.getHighlighted()) {
                highlightedItems.add(H.getItem());
            }
        }
        return highlightedItems;
    }

    public void resetHighlightedItems() {
        for (RecyclerViewHolder H : recyclerViewHolderList) {
            H.resetHighlight();
        }
    }
}
