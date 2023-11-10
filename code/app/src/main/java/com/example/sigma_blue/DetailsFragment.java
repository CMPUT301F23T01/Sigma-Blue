package com.example.sigma_blue;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sigma_blue.databinding.DetailsFragmentBinding;

import java.text.SimpleDateFormat;

/**
 * Class for handling activity to view details of an item
 */
public class DetailsFragment extends Fragment
{
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

    /**
     * Required empty public constructor
     */
    public DetailsFragment() {
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

    /**
     * Method to set details of item in fragment and handle button interactions
     * @param view is the View of the fragment
     * @param savedInstanceState is a Bundle passed that holds data of activity
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        final AddEditActivity activity = (AddEditActivity) getActivity();

        // Access item from parent activities ViewModel
        AddEditViewModel sharedVM = new ViewModelProvider(requireActivity()).get(AddEditViewModel.class);
        final Item currentItem = sharedVM.getItem().getValue();

        // set item details from bundle
        textName.setText(currentItem.getName());
        textValue.setText(String.valueOf(currentItem.getValue()));
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        textDate.setText(sdf.format(currentItem.getDate()));
        textMake.setText(currentItem.getMake());
        textModel.setText(currentItem.getModel());
        textSerial.setText(currentItem.getSerialNumber());
        textDescription.setText(currentItem.getDescription());
        textComment.setText(currentItem.getComment());

        view.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.action_detailsFragment_to_editFragment);
            }
        });

        view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sharedVM.setDeleteFlag(true);
                activity.returnAndClose();
            }
        });

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activity.returnAndClose();
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
}
