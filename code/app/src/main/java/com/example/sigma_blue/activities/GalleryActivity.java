package com.example.sigma_blue.activities;

import static com.example.sigma_blue.activities.ActivityLauncher.registerForActivityResult;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Gallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.example.sigma_blue.context.GlobalContext;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        globalContext = GlobalContext.getInstance();

        checkAndroidStoragePermissions();

        if (storagePermissionGranted) {
            // CALL FOR
        }

        // trial for asking user the image source is gallery or camera
        // TODO: should be refactored elsewhere, in edit fragment
        chooseImage(GalleryActivity.this);
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


    // function to let's the user to choose image from camera or gallery
    // TODO: SHOULD NOT BE HERE, (for testing purpose), should be refactored to somewhere else
    // may put in add/edit fragment
    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    // * change this content to open ImageTakingActivity
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    // * change this content to open GalleryActivity
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

}



