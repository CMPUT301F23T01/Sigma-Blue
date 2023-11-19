package com.example.sigma_blue.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.sigma_blue.context.GlobalContext;

public class PhotoTakingActivity extends BaseActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private GlobalContext globalContext;        // Global context object

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState); // Activity super

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
}
