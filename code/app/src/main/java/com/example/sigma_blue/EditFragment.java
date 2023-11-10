package com.example.sigma_blue;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sigma_blue.databinding.EditFragmentBinding;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Class for handling fragment for editing an Item objects values
 */
public class EditFragment extends Fragment
{
    // Fragment key-value pairs received from external fragments
    public static final String ARG_ITEM = "item";
    public static final String ARG_MODE = "mode";
    public static final String ARG_TAGS = "tag";

    private Item currentItem;

    private AddEditViewModel sharedVM;

//    private Item currentItem;
    private String mode;
    private String oldItemID;

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
    private ArrayList<EditText> editTextList;
    private int mDay, mMonth, mYear;

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

        // Access item from parent activities ViewModel
        sharedVM = new ViewModelProvider(activity).get(AddEditViewModel.class);
        final Item currentItem = sharedVM.getItem().getValue();
        final String mode = sharedVM.getMode().getValue();

        // set item details
        if (Objects.equals(mode, "edit"))
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
                if (Objects.equals(mode, "add"))
                {
                    // Cancel new item; Return to ViewListActivity
                    activity.returnAndClose();
                }
                else
                {
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
                // Open TagManager
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_tagManagerFragment);
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
                    loadUiText(currentItem);
                    sharedVM.setItem(currentItem);
                    sharedVM.setMode("edit");
                    sharedVM.setDeleteFlag(false);
                    NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
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
        try {
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
}
