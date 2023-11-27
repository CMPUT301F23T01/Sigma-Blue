package com.example.sigma_blue.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.activities.GalleryActivity;
import com.example.sigma_blue.activities.ImageTakingActivity;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.image.ImageListAdapter;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.entity.tag.TagListAdapter;
import com.example.sigma_blue.databinding.EditFragmentBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.VerifyException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Class for handling fragment for editing an Item objects values
 */
public class EditFragment extends Fragment
{
    //private AddEditViewModel sharedVM;

    // Fragment binding
    private EditFragmentBinding binding;

    // Fragment ui components
    private EditText textName;
    private EditText textValue;
    private EditText textDate;
    private EditText textMake;
    private EditText textModel;
    private EditText textSerial;
    private EditText textDescription;
    private EditText textComment;
    private ListView tagListView;
    private TagListAdapter tagListAdapter;
    private ListView itemImageList;
    private ImageListAdapter imageListAdapter;
    private ImageView itemImage;
    private ArrayList<EditText> editTextList;
    //private Item savedItemChanges;
    private int mDay, mMonth, mYear;
    private GlobalContext globalContext;

    /**
     * Required empty public constructor
     */
    public EditFragment() {
    }

    /**
     * Method to create the activity
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Method to inflate layout of fragment and bind components
     * @param inflater is the LayoutInflater that is going to inflate for the fragment
     * @param container is a ViewGroup of the views for the fragment
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = EditFragmentBinding.inflate(inflater, container, false);

        // bind ui components
        editTextList = new ArrayList<>();
        textName = binding.getRoot().findViewById(R.id.text_name_disp); editTextList.add(textName);
        textValue = binding.getRoot().findViewById(R.id.text_value_disp); editTextList.add(textValue);
        textDate = binding.getRoot().findViewById(R.id.text_date_disp); editTextList.add(textDate);
        textMake = binding.getRoot().findViewById(R.id.text_make_disp); editTextList.add(textMake);
        textModel = binding.getRoot().findViewById(R.id.text_model_disp); editTextList.add(textModel);
        textSerial = binding.getRoot().findViewById(R.id.text_serial_disp);
        textDescription = binding.getRoot().findViewById(R.id.text_description_disp);
        textComment = binding.getRoot().findViewById(R.id.text_comment_disp);
        tagListView = binding.getRoot().findViewById((R.id.list_tag));
        itemImageList = binding.getRoot().findViewById(R.id.list_pictures);

        // TODO add buttons here
        return binding.getRoot();
    }

    /**
     * Binding the current item to the ui.
     * @param currentItem is the item that is being edited.
     */
    private void editItemUIBindings(final Item currentItem) {
        textName.setText(currentItem.getName());
        textValue.setText(String.valueOf(currentItem.getValue()));
        textMake.setText(currentItem.getMake());
        textModel.setText(currentItem.getModel());
        textSerial.setText(currentItem.getSerialNumber());
        textDescription.setText(currentItem.getDescription());
        textComment.setText(currentItem.getComment());
    }

    /**
     * Method to set details of item in fragment and handle button interactions
     * @param view is the View of the fragment
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final AddEditActivity activity = (AddEditActivity) requireActivity();

        globalContext = GlobalContext.getInstance();
        // Load Item and mode
        Item modifiedItem = globalContext.getModifiedItem();

        tagListAdapter = TagListAdapter.newInstance(globalContext.getModifiedItem().getTags(), getContext());
        tagListView.setAdapter(tagListAdapter);

        imageListAdapter = new ImageListAdapter(getContext());
        itemImageList.setAdapter(imageListAdapter);

        globalContext.getImageManager().setAdapter(imageListAdapter);

        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        textDate.setText(sdf.format(modifiedItem.getDate()));

        Context context = this.getContext();
        textDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener()
                        {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day)
                            {
                                // TODO: Add this to strings.xml
                                textDate.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
                    // Cancel new item; Return to ViewListActivity
                    globalContext.setCurrentItem(null);
                    globalContext.newState(ApplicationState.VIEW_LIST_ACTIVITY);
                    Log.i("NEW STATE", ApplicationState.VIEW_LIST_ACTIVITY
                            .toString());
                    activity.returnAndClose();

                } else {
                    // Navigate to Item Details
                    NavHostFragment.findNavController(EditFragment.this)
                            .navigate(R.id
                                    .action_editFragment_to_detailsFragment);
                }
            }
        });


        view.findViewById(R.id.button_tag).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Save current ui state
                loadUiText(globalContext.getModifiedItem());
                // Open TagManager
                globalContext.newState(ApplicationState.TAG_MANAGER_FRAGMENT);
                Log.i("NEW STATE", ApplicationState.TAG_MANAGER_FRAGMENT
                        .toString());
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_tagManagerFragment);
            }
        });

        view.findViewById(R.id.add_image_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleImageClick();
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Load ui text and save into shared item; Navigate to DetailsFragment
                if (verifyText()) {
                    // need a new item as to not overwrite the old one. If the
                    // old one is overwritten then we don't know which item in
                    // the list needs to be deleted if doing an edit.
                    Item oldItem = globalContext.getCurrentItem();
                    Item newItem = globalContext.getModifiedItem();
                    loadUiText(newItem);

                    // State control for adding items
                    if (globalContext.getCurrentState() == ApplicationState.ADD_ITEM_FRAGMENT) {
                        if (globalContext.getItemList().getList().contains(newItem)) {
                            Snackbar errorSnackbar = Snackbar.make(v, "Item Already Exists", Snackbar.LENGTH_LONG);
                            errorSnackbar.show();
                        } else {
                            globalContext.getItemList().add(newItem);
                            globalContext.newState(ApplicationState
                                    .VIEW_LIST_ACTIVITY);
                            Log.i("NEW STATE", ApplicationState
                                    .VIEW_LIST_ACTIVITY.toString());
                            activity.returnAndClose();
                        }
                    } else if (globalContext.getCurrentState() == ApplicationState.EDIT_ITEM_FRAGMENT) {
                        globalContext.getItemList().updateEntity(newItem, oldItem);
                        globalContext.setCurrentItem(newItem);
                        globalContext.newState(ApplicationState.DETAILS_FRAGMENT);
                        NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
                    } else {
                        Log.e("BAD STATE",
                                "Edit and the item doesn't exist");
                        throw new VerifyException("Bad state"); // Unhandled
                    }

                }
            }
        });

        view.findViewById(R.id.button_barcode).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                globalContext.newState(ApplicationState.BARCODE_ADD_ACTIVITY);
                Intent intent = new Intent(v.getContext(), ImageTakingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        globalContext.getImageManager().updateFromItem(globalContext.getModifiedItem());
        if (globalContext.getCurrentState() == ApplicationState.EDIT_ITEM_FRAGMENT) {
            editItemUIBindings(globalContext.getModifiedItem());
        }
    }

    /**
     * Method for destroying fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Verifies that no text field is empty when saving edits
     * @return flag verifying that required EditText's are populated
     */
    private boolean verifyText() {
        String emptyErrText = "Must enter a value before saving";
        boolean flag = true;
        for (EditText e : editTextList)
        {
            if (TextUtils.isEmpty(e.getText()))
            {
                e.setError(emptyErrText);
                flag = false;
            }
        }
        return flag;
    }

    /**
     * Adds ui text into an item object
     * @param item to edit
     */
    private void loadUiText(@NonNull Item item)
    {
        item.setName(textName.getText().toString());
        try {
            item.setValue(Double.parseDouble(textValue.getText().toString()));
        } catch (java.lang.NumberFormatException e) {
            // no number entered
        }
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        try
        {
            item.setDate(sdf.parse(textDate.getText().toString()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        item.setMake(textMake.getText().toString());
        item.setModel(textModel.getText().toString());
        item.setSerialNumber(textSerial.getText().toString());
        item.setDescription(textDescription.getText().toString());
        item.setComment(textComment.getText().toString());
    }

    //TODO. make it so that you don't need to have a valid item before adding a picture
    private void handleImageClick() {
        /*

        Intent intent = new Intent(this.getContext(), ImageTakingActivity.class);
        loadUiText(globalContext.getModifiedItem());
        globalContext.newState(ApplicationState.IMAGE_ADD_ACTIVITY);
        startActivity(intent);

        */

        chooseImageSource(this.getContext());
    }


    // function to let's the user to choose image from camera or gallery
    // TODO: Beautify
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
                    loadUiText(globalContext.getModifiedItem());
                    globalContext.newState(ApplicationState.IMAGE_ADD_ACTIVITY);
                    startActivity(intent);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    // * change this content to open GalleryActivity
                    Intent intent = new Intent(context, GalleryActivity.class);
                    loadUiText(globalContext.getModifiedItem());
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
