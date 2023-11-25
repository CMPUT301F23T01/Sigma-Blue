package com.example.sigma_blue.entity.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sigma_blue.adapter.ASelectableListAdapter;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class ImageList {
    protected ArrayList<String> entityList;
    protected ImageDB dbHandler;
    protected ImageListAdapter adapter;

    public ImageList() {
        this.entityList = new ArrayList<>();
        this.dbHandler = new ImageDB();
    }

    /**
     * Upload a new item to the DB
     * @param account account to upload item as
     * @return the DB path to the new image
     */
    public String add(Account account, Bitmap bitmap) {
        return dbHandler.addImage(bitmap, account, task -> onImageUpload());
    }

    public void updateFromItem(Item item) {
        setList(item.getImagePaths());
    }

    public void setList(final ArrayList<String> list) {
        this.entityList = list;
        this.adapter.setList(new ArrayList<>());
        for (String s : entityList) {
            dbHandler.getImage(s, this::onImageDownload);
        }
    }
    public ImageDB getDbHandler() {
        return dbHandler;
    }
    public ImageListAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ImageListAdapter adapter) {
        this.adapter = adapter;
    }
    private void onImageDownload(byte[] bytes) {
        Log.i("ImageDownload", "Image download succeed");
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        adapter.add(bitmap);
        adapter.notifyDataSetChanged();
    }
    private void onImageUpload() {
        Log.i("DEBUG", "Upload Task Complete!");
    }
}
