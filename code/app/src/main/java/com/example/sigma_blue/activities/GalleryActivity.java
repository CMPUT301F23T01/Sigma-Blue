package com.example.sigma_blue.activities;

import static com.example.sigma_blue.activities.ActivityLauncher.registerForActivityResult;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Gallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.image.ImageDB;

public class GalleryActivity extends BaseActivity {

    static final int REQUEST_GALLERY_PICKING = 3;

    private GlobalContext globalContext;        // Global context object

    private boolean storagePermissionGranted;


    /*  Photo Picker trial:
        working on android 11, ugly UI uses android default file explorer
        not working on android 8
        discarded for now

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());


    }

     */

    // Intent Gallery picker
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set xml view
        setContentView(R.layout.photo_loading_activity);

        globalContext = GlobalContext.getInstance();

        checkAndroidStoragePermissions();

        if (storagePermissionGranted) {
            StartGallery();
        }

        // trial for asking user the image source is gallery or camera
        // TODO: should be refactored elsewhere, in edit fragment
        // chooseImage(GalleryActivity.this);
    }

    private void StartGallery(){
        Intent galleryPhotoPick  = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(galleryPhotoPick , REQUEST_GALLERY_PICKING);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private void checkAndroidStoragePermissions() {
        int permissionStatus = ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        storagePermissionGranted = (permissionStatus == 0);

        if (!storagePermissionGranted) {
            // need to ask for permission
            String[] p = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(p, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int r, String[] p, int[] g) {
        super.onRequestPermissionsResult(r, p, g);
        if (g.length > 0 && g[0] == 0) {
            // If the storage permission is successfully granted
            storagePermissionGranted = true;
            StartGallery();
        } else {
            // If the user doesn't want out app to use the camera go back to the edit page
            globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);

            finish();
        }
    }

    // check and upload the image after user selection
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();

        if (requestCode == REQUEST_GALLERY_PICKING && resultCode == RESULT_OK) {

            // extract image from path of image, then upload to the
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    //decode and compress the image to target for uploading and displaying
                    Bitmap imageBitmap = globalContext.getImageManager().compressBitmap(picturePath, 1000, 1000);
                    cursor.close();

                    String path = globalContext.getImageManager().uploadImage(globalContext.getAccount(), imageBitmap);
                    globalContext.getModifiedItem().addImagePath(path);
                    globalContext.newState(globalContext.getLastState());
                    finish();
                }
            }


        }
    }

}



