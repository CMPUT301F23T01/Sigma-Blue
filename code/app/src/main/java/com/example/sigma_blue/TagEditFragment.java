package com.example.sigma_blue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * Fragment for editing existing tags.
 */
public class TagEditFragment extends Fragment {
    private int tagColor = Color.parseColor("#0437f2"); // Default tag color, can change later
    private Tag tag;
    private TagEditFragment.OnFragmentInteractionListener listener;

    /**
     * Attaches the listener to this fragment where we will implement the interfaces
     * in the activity/fragment that calls the @code{editExistingTag} method.
     * @param context Application environment provided by default.
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TagEditFragment.OnFragmentInteractionListener) {
            listener = (TagEditFragment.OnFragmentInteractionListener) context;
        }
    }

    /**
     * Interface for editing an existing tag. To be implemented in the
     * activity/fragment itself that calls this fragment.
     * Note that you will have to update the dataset, as well as the
     * ArrayAdapter that is being used in this case in the activity.
     */
    public interface OnFragmentInteractionListener {
        void editExistingTag(String newName, Color newColor);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final EditText inputField = view.findViewById(R.id.input_field);
        final Button backButton = view.findViewById(R.id.back_button);
        final Button confirmButton = view.findViewById(R.id.confirm_button);

        // TODO Maybe put a color picker for the Tag class, maybe.
        // It might also be nice to have the color picker remember the last pick.

        Bundle args = getActivity().getIntent().getExtras();
        if (args == null) {

        } else {
            tag = args.getParcelable("TAG_TO_EDIT");
        }


        backButton.setOnClickListener(v -> {
            // exit fragment?
            getActivity().onBackPressed();
        });

        confirmButton.setOnClickListener(v -> {
            String tagName = inputField.getText().toString();
            Color tagColour = tag.getColour(); // TODO finish implementation, this will depend on how we select the color.

            // TODO Edit the existing tag, through the activity/fragment that calls this fragment.
            listener.editExistingTag(tagName, tagColour);

            getActivity().onBackPressed();

        });

        // The user cannot leave an empty tag
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                boolean inputEmpty = inputField.getText().length() == 0;
                if (inputEmpty) {confirmButton.setEnabled(false);} else {confirmButton.setEnabled(true);}
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_tag_fragment, container, false);
    }

}
