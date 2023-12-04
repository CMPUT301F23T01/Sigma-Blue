package com.example.sigma_blue.entity.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapts a list of strings (each representing a firebase storage path) into a displayed list of
 * images.
 */
public class ImageListAdapterFromPath extends RecyclerView.Adapter<ImageListAdapterFromPath.ImageViewHolder> {
    private Context mContext;
    private ArrayList<String> pathData;
    private OnItemClickListener mListener;

    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    private boolean isMenuNeeded;

    /**
     * Constructor that starts empty
     *
     * @param context the Context of the calling fragment or activity that the adapter is linked to.
     */
    public ImageListAdapterFromPath(Context context) {
        this.pathData = new ArrayList<String>();
        this.mContext = context;
        this.isMenuNeeded = true;
    }

    /**
     * Constructor that starts an empty adapter.
     * @param mContext context of calling activity/fragment
     * @param isMenuNeed true if there should be a delete menu on clicking an item
     */
    public ImageListAdapterFromPath(Context mContext, boolean isMenuNeed) {
        this.mContext = mContext;
        this.pathData = new ArrayList<String>();
        this.isMenuNeeded = isMenuNeed;
    }

    /**
     * RecyclerView.Adapter Called when RecyclerView needs a new RecyclerView.ViewHolder of the
     * given type to represent an item.
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return
     */
    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_row, parent, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {
        String path = pathData.get(position);
        // Reference to an image file in Cloud Storage
        StorageReference storageReference = storage.getReference(path);


        Glide.with(mContext)
                .load(storageReference)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pathData.size();
    }


    // get viewholder in
    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_list_image);

            itemView.setOnClickListener(this);
            if (isMenuNeeded) {
                itemView.setOnCreateContextMenuListener(this);
            }
        }


        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 2, 2, "Cancel");
            MenuItem delete = menu.add(Menu.NONE, 1, 1, "Delete");

            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    switch (item.getItemId()) {
                        case 1:
                            mListener.onDeleteClick(position);
                            return true;
                        case 2:
                            mListener.onWhatEverClick(position);
                            return true;
                    }
                }
            }
            return false;
        }

    }


    public interface OnItemClickListener {
        void onItemClick(int position);

        void onWhatEverClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public ArrayList<String> getPathData() {
        return pathData;
    }

    public void setPathData(ArrayList<String> pathData) {
        this.pathData = pathData;
    }
}
