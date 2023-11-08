package com.example.sigma_blue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TagListAdapter extends
        RecyclerView.Adapter<TagListAdapter.RecyclerViewHolder> {

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View color;

        /* Constructor that accepts the entire row */
        public RecyclerViewHolder(View tagView) {
            super(tagView);
            name = tagView.findViewById(R.id.tagName);
            color = tagView.findViewById(R.id.tagColor);
        }
    }

    /* Attributes */
    private TagList tagList;
    private Context context;

    /* Factories and Constructors */

    /**
     * Base factor that takes in a tagList object and returns the adapter for it
     * @param tagList is a TagList object, containing the types.
     * @return a TagListAdapter object that has been instantiated.
     */
    public static TagListAdapter newInstance(Context context, TagList tagList) {
        return new TagListAdapter(context, tagList);
    }

    /**
     * Basic constructor that takes in the TagList that will be adapted to a list view.
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     * @param tagList the tagList object that we will display on lists through this adapter.
     */
    public TagListAdapter(Context context, TagList tagList) {
        this.context = context;
        this.tagList = tagList;
    }

    /**
     * This is called when RecyclerView needs a new {@link RecyclerViewHolder}, of the given type to
     * represent an item.
     * <p>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type; in this case we inflate it from an XML layout file.
     * </p>
     * This new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerViewHolder, int, List)}. Since we will reuse this ViewHolder
     * to display different items in the data set, it would be a good idea to cache references
     * to sub views of the View to avoid unnecessary {@link View#findViewById(int)} calls...
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerViewHolder, int)
     */
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View tagView = inflater.inflate(R.layout.tag_row, parent, false);
        return new RecyclerViewHolder(tagView);
    }

    /**
     * This is called by RecyclerView to display the data at the specific position. As with
     * ItemListAdapter, this method should update the contents of the {@link RecyclerViewHolder#itemView}
     * to reflect the item at the given position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link ItemListAdapter.RecyclerViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Tag tag = tagList.getItem(position);

        // Assign content of the view holder
        holder.name.setText(tag.getTagText());
        holder.color.setBackgroundColor(tag.getColour().toArgb());
    }

    /**
     * Returns the total number of tags held by the adapter.
     *
     * @return The total number of tags held by the adapter.
     */
    @Override
    public int getItemCount() {
        return tagList.getCount();
    }

    public void addTag(Tag tag) {
        this.tagList.addTag(tag);
        notifyItemChanged(tagList.getCount() - 1);
    }

    public void removeTag(int position) {
        this.tagList.removeTag(position);
    }
}
