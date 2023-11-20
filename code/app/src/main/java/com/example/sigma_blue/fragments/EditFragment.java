package com.example.sigma_blue.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
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
import com.example.sigma_blue.activities.PhotoTakingActivity;
import com.example.sigma_blue.activities.ViewListActivity;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.tag.TagListAdapter;
import com.example.sigma_blue.databinding.EditFragmentBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.VerifyException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

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
    private ImageView itemImage;
    private ArrayList<EditText> editTextList;
    //private Item savedItemChanges;
    private int mDay, mMonth, mYear;
    private GlobalContext globalContext;

    private FirebaseStorage storage = FirebaseStorage.getInstance();

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
        itemImage = binding.getRoot().findViewById(R.id.item_image);

        return binding.getRoot();
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
        Item currentItem = globalContext.getCurrentItem();
        if (currentItem == null) {
            currentItem = new Item();
            globalContext.setCurrentItem(currentItem);
        }
        final String mode = globalContext.getCurrentState();

        // set item details
        if (Objects.equals(mode, "edit_item_fragment"))
        {
            textName.setText(currentItem.getName());
            textValue.setText(String.valueOf(currentItem.getValue()));
            textMake.setText(currentItem.getMake());
            textModel.setText(currentItem.getModel());
            textSerial.setText(currentItem.getSerialNumber());
            textDescription.setText(currentItem.getDescription());
            textComment.setText(currentItem.getComment());
            tagListAdapter = TagListAdapter.newInstance(currentItem.getTags(), getContext());
            tagListView.setAdapter(tagListAdapter);
        }
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        textDate.setText(sdf.format(currentItem.getDate()));

        //ITEM IMAGE RELATED CHANGES
        // trying to get the path of image, and put it on the add item
        String tempImagePath = globalContext.getCurrentItem().getPhotoPath();
        // set the image of the item
        // Create a storage reference from our app
        if (tempImagePath != null) {
            StorageReference storageRef = storage.getReference();
            StorageReference itemImageRef = storageRef.child(tempImagePath);

            final long ONE_MEGABYTE = 1024 * 1024;
            itemImageRef.getBytes(10 * ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                    Log.i("ImageDownload", "Image download succeed");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    itemImage.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }

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
                if (Objects.equals(mode, "add_item_fragment")) {
                    // Cancel new item; Return to ViewListActivity
                    globalContext.setCurrentItem(null);
                    globalContext.newState("view_list_activity");
                    activity.returnAndClose();

                } else {
                    // Navigate to Item Details
                    NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
                }
            }
        });


        view.findViewById(R.id.button_tag).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Save current ui state
                loadUiText(globalContext.getCurrentItem());
                // Open TagManager
                globalContext.newState("tag_manager_fragment");
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_tagManagerFragment);
            }
        });

        view.findViewById(R.id.item_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFigureClick(globalContext.getCurrentItem());

                //PHOTO TAKING ACTIVITY
                // for back to main logic, can be improved in the future
                activity.finish();
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Load ui text and save into shared item; Navigate to DetailsFragment
                if (verifyText())
                {
                    // need a new item as to not overwrite the old one. If the old one is overwritten
                    // then we don't know which item in the list needs to be deleted if doing an edit.
                    Item modifiedItem = new Item();
                    loadUiText(modifiedItem);
                    if (Objects.equals(globalContext.getCurrentState(), "add_item_fragment")) {
                        if (globalContext.getItemList().getList().contains(modifiedItem)) {
                            Snackbar errorSnackbar = Snackbar.make(v, "Item Already Exists", Snackbar.LENGTH_LONG);
                            errorSnackbar.show();
                        } else {
                            globalContext.getItemList().add(modifiedItem);
                            globalContext.setCurrentItem(modifiedItem);
                            globalContext.newState("view_list_activity");
                            activity.returnAndClose();
                        }
                    } else if (Objects.equals(globalContext.getCurrentState(), "edit_item_fragment")) {
                        globalContext.getItemList().updateItem(modifiedItem, globalContext.getCurrentItem());
                        globalContext.setCurrentItem(modifiedItem);
                        globalContext.newState("details_fragment");
                        NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
                    } else {
                        throw new VerifyException("Bad state");
                    }

                }
            }
        });
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
    private boolean verifyText()
    {
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
        item.setValue(Float.parseFloat(textValue.getText().toString()));
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
        //Photo Added, should have better way
        item.setPhotoPath(globalContext.getCurrentItem().getPhotoPath());
    }

    /**
     * listener used to deal with the user clicking on a thing in the list. This method is passed to
     * the ItemList constructor as a callback function, but not called directly from anywhere.
     * see <a href="https://stackoverflow.com/questions/24471109/recyclerview-onclick">...</a>,
     * answer from Marurban
     * @param item Clicked on item
     */
    private void handleFigureClick(Item item) {
        Log.i("DEBUG", item.getName() + "Short Press");
        Intent intent = new Intent(this.getContext(), PhotoTakingActivity.class);
        // TODO ISSUE: not going to change the state at this time, seems messed up existing logic between activities/frags
        //globalContext.newState("add_photo");
        startActivity(intent);
    }
}
