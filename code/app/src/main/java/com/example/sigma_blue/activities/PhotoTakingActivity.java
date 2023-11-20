package com.example.sigma_blue.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PhotoTakingActivity extends BaseActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private GlobalContext globalContext;        // Global context object

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); // Activity super
        globalContext = GlobalContext.getInstance();

        // Set xml view
        setContentView(R.layout.photo_loading_activity);

        dispatchTakePictureIntent();




    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent Data) {

        super.onActivityResult(requestCode, resultCode, Data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = Data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            byte[] data = outputStream.toByteArray();

            String path = globalContext.getAccount().getUsername() + "/" + UUID.randomUUID() +".png";
            globalContext.getCurrentItem().setPhotoPath(path);
            StorageReference firebaseStorageRef = storage.getReference(path);

            //now upload the data(image), expected to take some time
            UploadTask uploadTask = firebaseStorageRef.putBytes (data);

            // TODO: build a load waiting screen

            uploadTask.addOnCompleteListener(PhotoTakingActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    // After the upload success, go back to the previous state activity
                    Log.i("MA", "Upload Task Complete!");
                    Intent intent = new Intent(PhotoTakingActivity.this, AddEditActivity.class);
                    // TODO ISSUE: not going to change the state at this time,
                    //  seems messed up existing logic between activities/frag
                    //globalContext.newState(globalContext.getLastState());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}
