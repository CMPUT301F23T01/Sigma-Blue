package com.example.sigma_blue.entity.image;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.sigma_blue.entity.account.Account;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

/**
 * Works with firebase storage to manage images
 */
public class ImageDB {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public String addImage(Bitmap imageBitmap, Account account, OnCompleteListener<UploadTask.TaskSnapshot> listener) {
        //return "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();

        String path = account.getUsername() + "/" + UUID.randomUUID() +".png";
        StorageReference firebaseStorageRef = storage.getReference(path);

        //now upload the data(image), expected to take some time
        UploadTask uploadTask = firebaseStorageRef.putBytes(data);
        uploadTask.addOnCompleteListener(listener);
        return path;
    }

    public void getImage(String path, OnSuccessListener<byte[]> listener) {
        StorageReference storageRef = storage.getReference();
        StorageReference itemImageRef = storageRef.child(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        itemImageRef.getBytes(10 * ONE_MEGABYTE).addOnSuccessListener(listener).
                addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("ImageDownload", "Image download fail");
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Log.e("ImageDownload", "Image download canceled");
            }
        });
    }
}
