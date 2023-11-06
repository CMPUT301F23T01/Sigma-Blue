package com.example.sigma_blue;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sigma_blue.databinding.DetailsFragmentBinding;

import java.text.DateFormat;
import java.util.Date;

public class DetailsFragment extends Fragment
{
    // Fragment key-value pairs received from external fragments
    private static final String ARG_ITEM = "item";

    private String mName = " ";
    private float mValue = 0f;
    private Date mDate = new Date();
    private String mMake = " ";
    private String mModel = " ";
    private String mSerial = " ";
    private String mDescription = " ";
    private String mComment = " ";

    // Fragment binding
    private DetailsFragmentBinding binding;

    // Fragment ui components
    TextView textName;
    TextView textValue;
    TextView textDate;
    TextView textMake;
    TextView textModel;
    TextView textSerial;
    TextView textDescription;
    TextView textComment;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load item from bundle
        if (getArguments() != null)
        {
            Item i = (Item)getArguments().getSerializable(ARG_ITEM);
            if (i != null)
            {
                mName = i.getName();
                mValue = i.getValue();
                mDate = i.getDate();
                mMake = i.getMake();
                mModel = i.getModel();
                mSerial = i.getSerialNumber();
                mDescription = i.getDescription();
                mComment = i.getComment();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = DetailsFragmentBinding.inflate(inflater, container, false);

        // bind ui components
        textName = binding.getRoot().findViewById(R.id.text_name_disp);
        textValue = binding.getRoot().findViewById(R.id.text_value_disp);
        textDate = binding.getRoot().findViewById(R.id.text_date_disp);
        textMake = binding.getRoot().findViewById(R.id.text_make_disp);
        textModel = binding.getRoot().findViewById(R.id.text_model_disp);
        textSerial = binding.getRoot().findViewById(R.id.text_serial_disp);
        textDescription = binding.getRoot().findViewById(R.id.text_description_disp);
        textComment = binding.getRoot().findViewById(R.id.text_comment_disp);

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

        view.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Item i = new Item(mName, mDate, mDescription, mComment, mMake, mModel, mValue);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ARG_ITEM, i);
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.action_detailsFragment_to_editFragment, bundle);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}