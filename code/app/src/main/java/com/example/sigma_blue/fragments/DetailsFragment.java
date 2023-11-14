package com.example.sigma_blue.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sigma_blue.context.AddEditViewModel;
import com.example.sigma_blue.R;
import com.example.sigma_blue.entity.tag.TagListAdapter;
import com.example.sigma_blue.activities.AddEditActivity;
import com.example.sigma_blue.databinding.DetailsFragmentBinding;
import com.example.sigma_blue.entity.item.item.Item;

import java.text.SimpleDateFormat;

/**
 * Class for handling activity to view details of an item
 */
public class DetailsFragment extends Fragment
{
    // Fragment binding
    private DetailsFragmentBinding binding;

    // Fragment ui components
    private TextView textName;
    private TextView textValue;
    private TextView textDate;
    private TextView textMake;
    private TextView textModel;
    private TextView textSerial;
    private TextView textDescription;
    private TextView textComment;
    private ListView tagListView;
    private TagListAdapter tagListAdapter;

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
        tagListView = binding.getRoot().findViewById(R.id.list_tag);

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
        AddEditViewModel sharedVM = new ViewModelProvider(activity).get(AddEditViewModel.class);
        final Item currentItem = sharedVM.getItem().getValue();

        // set item details from shared activity
        textName.setText(currentItem.getName());
        textValue.setText(String.valueOf(currentItem.getValue()));
        SimpleDateFormat sdf = new SimpleDateFormat(getResources().getString(R.string.date_format));
        textDate.setText(sdf.format(currentItem.getDate()));
        textMake.setText(currentItem.getMake());
        textModel.setText(currentItem.getModel());
        textSerial.setText(currentItem.getSerialNumber());
        textDescription.setText(currentItem.getDescription());
        textComment.setText(currentItem.getComment());
        tagListAdapter = TagListAdapter.newInstance(currentItem.getTags(), getContext());
        tagListView.setAdapter(tagListAdapter);

        view.findViewById(R.id.button_edit).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Navigate to EditFragment
                sharedVM.setEditItem(currentItem);
                NavHostFragment.findNavController(DetailsFragment.this).navigate(R.id.action_detailsFragment_to_editFragment);
            }
        });

        view.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                // Return to ViewListActivity; notify object needs to be deleted
                sharedVM.setDeleteFlag(true);
                activity.returnAndClose();
            }
        });

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Return to ViewListActivity
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
