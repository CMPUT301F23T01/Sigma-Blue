package com.example.sigma_blue;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sigma_blue.databinding.EditFragmentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditFragment extends Fragment
{
    // Fragment key-value pairs received from external fragments
    private static final String ARG_ITEM = "item";

    private Item currentItem;
    private String mName = " ";
    private float mValue = 0f;
    private Date mDate = new Date();
    private String mMake = " ";
    private String mModel = " ";
    private String mSerial = " ";
    private String mDescription = " ";
    private String mComment = " ";

    // Fragment binding
    private EditFragmentBinding binding;

    // Fragment ui components
    EditText textName;
    EditText textValue;
    EditText textDate;
    EditText textMake;
    EditText textModel;
    EditText textSerial;
    EditText textDescription;
    EditText textComment;
    ArrayList<EditText> editTextList;

    public EditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load item from bundle
        if (getArguments() != null)
        {
            currentItem = (Item)getArguments().getSerializable(ARG_ITEM);
            if (currentItem != null)
            {
                mName = currentItem.getName();
                mValue = currentItem.getValue();
                mDate = currentItem.getDate();
                mMake = currentItem.getMake();
                mModel = currentItem.getModel();
                mSerial = currentItem.getSerialNumber();
                mDescription = currentItem.getDescription();
                mComment = currentItem.getComment();
            }
        }
    }

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
        textSerial = binding.getRoot().findViewById(R.id.text_serial_disp); editTextList.add(textSerial);
        textDescription = binding.getRoot().findViewById(R.id.text_description_disp); editTextList.add(textDescription);
        textComment = binding.getRoot().findViewById(R.id.text_comment_disp); editTextList.add(textComment);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // set item details from bundle
        textName.setText(mName);
        textValue.setText(String.valueOf(mValue));
        textDate.setText(mDate.toString());
        textMake.setText(mMake);
        textModel.setText(mModel);
        textSerial.setText(mSerial);
        textDescription.setText(mDescription);
        textComment.setText(mComment);

        view.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
            }
        });

        view.findViewById(R.id.button_save).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (verifyText())
                {
                    Bundle bundle = new Bundle();
                    currentItem.setName(textName.getText().toString());
                    currentItem.setValue(Float.valueOf(textValue.getText().toString()));
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    try {
                        currentItem.setDate(sdf.parse(textDate.getText().toString()));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    currentItem.setMake(textMake.getText().toString());
                    currentItem.setModel(textModel.getText().toString());
                    currentItem.setSerialNumber(textSerial.getText().toString());
                    currentItem.setDescription(textDescription.getText().toString());
                    currentItem.setComment(textComment.getText().toString());
                    bundle.putSerializable(ARG_ITEM, currentItem);
                    NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment, bundle);
                }

            }
        });
    }

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
}