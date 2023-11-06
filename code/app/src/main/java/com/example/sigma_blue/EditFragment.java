package com.example.sigma_blue;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sigma_blue.databinding.DetailsFragmentBinding;
import com.example.sigma_blue.databinding.EditFragmentBinding;

import java.text.DateFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NAME = "name";
    private static final String ARG_VALUE = "value";
    private static final String ARG_DATE = "date";
    private static final String ARG_MAKE = "make";
    private static final String ARG_MODEL = "model";
    private static final String ARG_SERIAL = "serial";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_COMMENT = "comment";

    private String mName;
    private Double mValue;
    private String mDate; //Is DateFormat sufficient for this?
    private String mMake;
    private String mModel;
    private int mSerial;
    private String mDescription;
    private String mComment;

    private EditFragmentBinding binding;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance(String param1, String param2) {
        EditFragment fragment = new EditFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_NAME);
            mValue = getArguments().getDouble(ARG_VALUE);
            mDate = getArguments().getString(ARG_DATE);
            mMake = getArguments().getString(ARG_MAKE);
            mModel = getArguments().getString(ARG_MODEL);
            mSerial = getArguments().getInt(ARG_SERIAL);
            mDescription = getArguments().getString(ARG_DESCRIPTION);
            mComment = getArguments().getString(ARG_COMMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = EditFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_detailsFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}