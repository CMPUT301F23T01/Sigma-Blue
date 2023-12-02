package com.example.sigma_blue.entity.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.Item;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * Provides an interface for the image database and image adapter classes
 */
public class ImageManager {
    private ArrayList<String> pathList;
    private ArrayList<Bitmap> entityList;
    protected ImageDB dbHandler;
    protected ImageListAdapterFromPath adapter;
    private Boolean uploading;
    private Boolean upToDate;

    public ImageManager() {
        this.pathList = new ArrayList<>();
        this.entityList = new ArrayList<>();
        this.dbHandler = new ImageDB();
        this.uploading = false;
        this.upToDate = true;
    }

    /**
     * Upload a new item to the DB. Image will be displayed right away for better responsiveness,
     * even if it should be attached to a different item. Once all uploads are done things should
     * sort themselves out.
     * @param account account to upload item as
     * @return the DB path to the new image
     */
    public String uploadImage(Account account, Bitmap bitmap) {
        this.uploading = true;
        this.entityList.add(bitmap);
        this.adapter.notifyDataSetChanged();
        return dbHandler.addImage(bitmap, account, task -> onImageUpload());
    }


    /**
     * use the image paths from the passed item to download the corresponding bitmaps
     * @param item item to get pictures from
     */
    public void updateFromItem(Item item) {
        this.pathList = item.getImagePaths();
        // avoid downloading images if there is an in progress upload (may try to download a missing image).

        if (this.uploading) {
            // set a flag to re-download everything once there are no uploads going.
            this.upToDate = false;
        } else {
            this.updateFromList();
        }
    }

    public ImageListAdapterFromPath getAdapter() {
        return adapter;
    }

    private void updateFromList() {
        this.entityList.clear();
//        for (String s : pathList) {
//            dbHandler.getImage(s, this::onImageDownload);
//        }
        for (int i = 0; i < pathList.size() ; i ++) {
            dbHandler.getImage(pathList.get(i), this::onImageDownload);
        }
        this.adapter.setPathData(this.pathList);
        this.adapter.notifyDataSetChanged();
        this.upToDate = true; // technically not up to date until the downloads finish, but this works
    }

    /**
     * Set the adapter and tie it to the list of image bitmaps held in this class.
     * @param adapter
     */
    public void setAdapter(ImageListAdapterFromPath adapter) {
        this.adapter = adapter;
        this.adapter.setPathData(pathList);
    }
    private void onImageDownload(byte[] bytes) {
        Log.i("DEBUG", "Image download succeed");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        this.entityList.add(bitmap);
        this.adapter.notifyDataSetChanged();
    }
    private void onImageUpload() {
        this.uploading = false;
        Log.i("DEBUG", "Upload Task Complete!");
        if (!upToDate) {
            this.updateFromList();
        }
        // in case this upload was blocking a download
    }

    public ArrayList<String> getPathList() {
        return this.pathList;
    }

    // bitmap compressing method
    public Bitmap compressBitmap(String path, int reqWidth, int reqHeight) {
        // get width and height of bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        // calculate compressing ratio
        // android suggest to compress image with ratio of 2^n
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            while ((halfWidth / inSampleSize) >= reqWidth
                    && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }
        // decode the image and compress it
        BitmapFactory.Options compressedOptions = new BitmapFactory.Options();
        compressedOptions.inSampleSize = inSampleSize;
        compressedOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, compressedOptions);
    }
}
