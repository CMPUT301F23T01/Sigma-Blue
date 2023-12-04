package com.example.sigma_blue.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.sigma_blue.R;
import com.example.sigma_blue.activities.ImageTakingActivity;
import com.example.sigma_blue.adapter.TabMode;
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.databinding.EditFragItemDetailsBinding;
import com.example.sigma_blue.databinding.DetailsFragItemDetailsBinding;
import com.example.sigma_blue.entity.item.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ItemDetailsFragment extends Fragment
{
    private final GlobalContext globalContext = GlobalContext.getInstance();
    private final TabMode mode;
    private ViewBinding binding;

    // General objects
    private TextView textValue;
    private TextView textDate;
    private TextView textMake;
    private TextView textModel;
    private TextView textSerial;
    private TextView textDescription;
    private TextView textComment;

    // Objects utilized exclusively by EditFragment
    private ArrayList<EditText> editTextList;
    private Button scanSerial, getDescription;
    private int mDay, mMonth, mYear;

    /**
     * Creates a new ItemDetails instance
     * @param mode indicates whether fragment is utilized in Details or Edit page ui
     */
    public ItemDetailsFragment(TabMode mode) {
        this.mode = mode;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Change binding to correspond to current UI and update additional ui components where applicable
        if (mode.equals(TabMode.Edit)) {
            binding = EditFragItemDetailsBinding.inflate(inflater, container, false);
            updateBinding(binding);

            scanSerial = binding.getRoot().findViewById(R.id.button_barcode);
            getDescription = binding.getRoot().findViewById(R.id.button_autofill);

            editTextList = new ArrayList<>();
            editTextList.add((EditText)textValue);
            editTextList.add((EditText)textDate);
            editTextList.add((EditText)textMake);
            editTextList.add((EditText) textModel);
        } else if (mode.equals(TabMode.Details)) {
            binding = DetailsFragItemDetailsBinding.inflate(inflater, container, false);
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
        // Initialize local variables
        globalContext.getDescriptionManager().setContext(getContext());
        Context context = this.getContext();

        // Setup UI components
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        textDate.setText(sdf.format(globalContext.getModifiedItem().getDate()));
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
                            public void onDateSet(DatePicker view, int year, int month, int day)
                            {
                                // TODO: Add this to strings.xml
                                textDate.setText(year + "-" + (month + 1) + "-" + day);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        if (mode == TabMode.Edit)
        {
            scanSerial.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    saveText();
                    globalContext.newState(ApplicationState.BARCODE_ADD_ACTIVITY);
                    Intent intent = new Intent(v.getContext(), ImageTakingActivity.class);
                    startActivity(intent);
                }
            });
            getDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveText();
                    globalContext.getDescriptionManager().updateItemDescription(
                            globalContext.getModifiedItem().getSerialNumber(),
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    globalContext.getModifiedItem().setDescription(response);
                                    textDescription.setText(response);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Snackbar errorSnackbar = Snackbar.make(view, "Failed to find a matching description", Snackbar.LENGTH_LONG);
                                    errorSnackbar.show();
                                    globalContext.getModifiedItem().setDescription("");
                                    textDescription.setText("");
                                }
                            });
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        editItemUIBindings(globalContext.getModifiedItem());
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
     * Updates ui text with values from a item objec
     * @param currentItem with updated text values
     */
    private void editItemUIBindings(final Item currentItem) {
        if (currentItem.getValue() != null) {
            textValue.setText(String.valueOf(currentItem.getValue()));
        } else {
            textValue.setText("");
        }
        textMake.setText(currentItem.getMake());
        textModel.setText(currentItem.getModel());
        textSerial.setText(currentItem.getSerialNumber());
        textDescription.setText(currentItem.getDescription());
        textComment.setText(currentItem.getComment());
    }

    /**
     * Adds ui text into an item object
     * @param item to edit
     */
    private void loadUiText(@NonNull Item item)
    {
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

    /**
     * Verifies that no text field is empty when saving edits
     * @return flag verifying that required EditText's are populated
     */
    public boolean verifyText() {
        if (mode == TabMode.Edit) {
            String emptyErrText = "Must enter a value before saving";
            boolean flag = true;
            for (EditText e : editTextList) {
                if (TextUtils.isEmpty(e.getText())) {
                    e.setError(emptyErrText);
                    flag = false;
                }
            }
            return flag;
        }
        else { return true; }
    }

    /**
     * updates ui text elements from global context
     */
    public void updateText() {
        Item item = globalContext.getModifiedItem();
        editItemUIBindings(item);
    }

    /**
     * Saves text in current ui to global context
     */
    public void saveText() {
        Item item = globalContext.getModifiedItem();
        loadUiText(item);
        globalContext.setModifiedItem(item);
    }

    /**
     * updates the ui elements to the current binding
     * @param binding of the current xml reference
     */
    public void updateBinding(ViewBinding binding)
    {
        textValue = binding.getRoot().findViewById(R.id.text_value_disp);
        textDate = binding.getRoot().findViewById(R.id.text_date_disp);
        textMake = binding.getRoot().findViewById(R.id.text_make_disp);
        textModel = binding.getRoot().findViewById(R.id.text_model_disp);
        textSerial = binding.getRoot().findViewById(R.id.text_serial_disp);
        textDescription = binding.getRoot().findViewById(R.id.text_description_disp);
        textComment = binding.getRoot().findViewById(R.id.text_comment_disp);
    }
}