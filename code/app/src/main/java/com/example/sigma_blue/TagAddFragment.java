package com.example.sigma_blue;

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
import androidx.fragment.app.Fragment;

/**
 * Fragment for adding new tags.
 */
public class TagAddFragment extends Fragment {
    private int tagColor = Color.parseColor("#0437f2"); // Default tag color, can change later

    public TagAddFragment() {
        super(R.layout.add_tag_fragment);
    }

    // I am pretty sure that isn't quite how it is done, I might have mixed up the Fragment methods
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final EditText inputField = view.findViewById(R.id.input_field);
        final Button backButton = view.findViewById(R.id.back_button);
        final Button confirmButton = view.findViewById(R.id.confirm_button);

        confirmButton.setEnabled(false);

        // TODO Maybe put a color picker for the Tag class, maybe.
        // It might also be nice to have the color picker remember the last pick.

        backButton.setOnClickListener(v -> {
            // exit fragment?
            getActivity().onBackPressed();
        });

        confirmButton.setOnClickListener(v -> {
            String tagName = inputField.getText().toString();

            // create a new tag object
            // add it to TagList

            // exit fragment?
            getActivity().onBackPressed();

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
        return inflater.inflate(R.layout.add_tag_fragment, container, false);
    }

}
