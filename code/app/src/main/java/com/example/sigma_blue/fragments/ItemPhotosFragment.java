package com.example.sigma_blue.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.example.sigma_blue.R;
import com.example.sigma_blue.activities.GalleryActivity;
import com.example.sigma_blue.activities.ImageTakingActivity;
import com.example.sigma_blue.adapter.TabMode;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.databinding.DetailsFragItemPhotosBinding;
import com.example.sigma_blue.databinding.EditFragItemPhotosBinding;
import com.example.sigma_blue.entity.image.ImageListAdapter;
import com.example.sigma_blue.entity.item.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ItemPhotosFragment extends Fragment
{
    private final GlobalContext globalContext = GlobalContext.getInstance();
    private final TabMode mode;
    private ViewBinding binding;

    private ListView itemImageList;
    private FloatingActionButton addPicture;

    public ItemPhotosFragment(TabMode mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Change binding to correspond to current UI and update additional ui components where applicable
        if (mode.equals(TabMode.Edit)) {
            binding = EditFragItemPhotosBinding.inflate(inflater, container, false);
            addPicture = binding.getRoot().findViewById(R.id.button_picture);
            updateBinding(binding);
        } else if (mode.equals(TabMode.Details)) {
            binding = DetailsFragItemPhotosBinding.inflate(inflater, container, false);
            updateBinding(binding);
        }
        else {
            throw new RuntimeException("Unhandled mode assigned to instance");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageListAdapter imageListAdapter = new ImageListAdapter(getContext());
        itemImageList.setAdapter(imageListAdapter);
        globalContext.getImageManager().setAdapter(imageListAdapter);

        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateImageList();
    }

    /**
     * Method for destroying fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void updateImageList()
    {

    }

    public void updateBinding(ViewBinding binding)
    {
        itemImageList = binding.getRoot().findViewById(R.id.list_pictures);
    }

    /**
     * Method to handle click on an item in the image list
     */
    private void handleImageClick() {
        /*

        Intent intent = new Intent(this.getContext(), ImageTakingActivity.class);
        loadUiText(globalContext.getModifiedItem());
        globalContext.newState(ApplicationState.IMAGE_ADD_ACTIVITY);
        startActivity(intent);

        */

        chooseImageSource(this.getContext());
    }

    /**
     * function to let's the user to choose image from camera or gallery
     * @param context from current fragment
     */
    private void chooseImageSource(Context context){
        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    // * change this content to open ImageTakingActivity
                    Intent intent = new Intent(context, ImageTakingActivity.class);
                    globalContext.newState(ApplicationState.IMAGE_ADD_ACTIVITY);
                    startActivity(intent);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    // * change this content to open GalleryActivity
                    Intent intent = new Intent(context, GalleryActivity.class);
                    globalContext.newState(ApplicationState.IMAGE_ADD_ACTIVITY);
                    startActivity(intent);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
}
