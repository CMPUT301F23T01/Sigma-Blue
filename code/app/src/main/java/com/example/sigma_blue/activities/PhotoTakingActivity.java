package com.example.sigma_blue.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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
    private boolean cameraPermissionGranted;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Activity super
        globalContext = GlobalContext.getInstance();
        // Set xml view
        setContentView(R.layout.photo_loading_activity);

        checkAndroidCameraPermissions();

        if (cameraPermissionGranted) {
            dispatchTakePictureIntent();
        }
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
            String path = globalContext.getImageDB().addImage(imageBitmap, globalContext.getAccount(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
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

            globalContext.getCurrentItem().addImagePath(path);
            // TODO: build a load waiting screen
        }
    }

    private void checkAndroidCameraPermissions() {
        int permissionStatus = ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.CAMERA);

        cameraPermissionGranted = (permissionStatus == 0);

        if (!cameraPermissionGranted) {
            // need to ask for permission
            String[] p = {android.Manifest.permission.CAMERA};
            this.requestPermissions(p, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int r, String[] p, int[] g) {
        super.onRequestPermissionsResult(r, p, g);
        if (g.length > 0 && g[0] == 0) {
            cameraPermissionGranted = true;
            dispatchTakePictureIntent();
        } else {
            // If the user doesn't want out app to use the camera go back to the edit page
            Intent intent = new Intent(PhotoTakingActivity.this, AddEditActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
