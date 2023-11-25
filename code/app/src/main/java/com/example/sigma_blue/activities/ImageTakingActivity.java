package com.example.sigma_blue.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.fragments.EditFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class ImageTakingActivity extends BaseActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_BARCODE_SCAN = 2;

    private GlobalContext globalContext;        // Global context object
    private boolean cameraPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Activity super
        globalContext = GlobalContext.getInstance();
        // Set xml view
        setContentView(R.layout.photo_loading_activity);

        checkAndroidCameraPermissions();

        if (cameraPermissionGranted) {
            dispatchScanOrImageIntent();
        }
    }

    private void dispatchScanOrImageIntent() {
        if (globalContext.getCurrentState() == ApplicationState.IMAGE_ADD_ACTIVITY) {
            dispatchTakePictureIntent();
        } else {
            dispatchScanBarcodeIntent();
        }
    }
    private void dispatchScanBarcodeIntent() {
        ScanOptions options = new ScanOptions();
        options.setOrientationLocked(true);
        options.setPrompt("Scan Barcode");
        options.setBeepEnabled(true);

        barcodeLauncher.launch(options);
    }
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Snackbar failedBarcodeSnackbar = Snackbar.make(findViewById(R.id.login_main), "Failed to scan", Snackbar.LENGTH_LONG);
                    failedBarcodeSnackbar.show();
                } else {
                    globalContext.getModifiedItem().setSerialNumber((String) result.getContents());
                }
                //Intent intent = new Intent(ImageTakingActivity.this, AddEditActivity.class);
                globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);
                //startActivity(intent);
                finish();
            });

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String path = globalContext.getImageList().add(globalContext.getAccount(), imageBitmap);
            globalContext.getCurrentItem().addImagePath(path);
            globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);
            finish();

        } else if (requestCode == REQUEST_BARCODE_SCAN && resultCode == RESULT_OK) {
            // TODO: FIX
            // doesn't work as of now. The callback registering is a temporary fix
//            if(extras.get("data") != null) {
//                globalContext.setSerialNumber((String) extras.get("data"));
//                //Intent intent = new Intent(ImageTakingActivity.this, AddEditActivity.class);
//                globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);
//                //startActivity(intent);
//                finish();
//            }
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
            dispatchScanOrImageIntent();
        } else {
            // If the user doesn't want out app to use the camera go back to the edit page
            //Intent intent = new Intent(ImageTakingActivity.this, AddEditActivity.class);
            globalContext.newState(ApplicationState.EDIT_ITEM_FRAGMENT);
            //startActivity(intent);
            finish();
        }
    }
}
