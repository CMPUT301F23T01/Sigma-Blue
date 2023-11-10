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
import androidx.navigation.fragment.NavHostFragment;

/**
 * Fragment for adding new tags.
 */
public class TagAddFragment extends Fragment {
    private int tagColor = Color.parseColor("#0437f2"); // Default tag color, can change later

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final EditText inputField = view.findViewById(R.id.input_field);
        final Button backButton = view.findViewById(R.id.back_button);
        final Button confirmButton = view.findViewById(R.id.confirm_button);

        confirmButton.setEnabled(false); // User cannot outright add an empty tag on startup

        // TODO Maybe put a color picker for the Tag class, maybe.
        // It might also be nice to have the color picker remember the last pick.

        backButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        confirmButton.setOnClickListener(v -> {
            String tagName = inputField.getText().toString();
            // NOTE for now, we will use the default color that is provided in the fragment.

            Tag tagToSend = new Tag(tagName, tagColor);

            Bundle bundle = new Bundle();
            bundle.putSerializable(TagManagerFragment.ARG_TAG_ADD, tagToSend);

            NavHostFragment.findNavController(TagAddFragment.this).navigate(R.id.action_tagAddFragment_to_tagManagerFragment, bundle);

        });

        // The user cannot add empty tags
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
        return inflater.inflate(R.layout.add_tag_fragment, container, false);
    }

}
