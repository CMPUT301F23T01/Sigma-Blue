package com.example.sigma_blue.fragments;

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

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.tag.Tag;

/**
 * Fragment for editing existing tags.
 */
public class TagEditFragment extends Fragment {
    private int tagColor = Color.parseColor("#0437f2"); // Default tag color, can change later
    private Tag tag;
    private GlobalContext globalContext;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        final EditText inputField = view.findViewById(R.id.input_field);
        final Button backButton = view.findViewById(R.id.back_button);
        final Button confirmButton = view.findViewById(R.id.confirm_button);

        // TODO Maybe put a color picker for the Tag class, maybe.
        // It might also be nice to have the color picker remember the last pick.

        globalContext = GlobalContext.getInstance();

        backButton.setOnClickListener(v -> {
            // exit fragment?
            globalContext.newState(globalContext.getLastState());
            getActivity().onBackPressed();
        });

        confirmButton.setOnClickListener(v -> {
            String tagName = inputField.getText().toString();

            globalContext.getTagList().updateEntity(new Tag(tagName, tagColor), globalContext.getHighlightedTags().get(0));
            globalContext.newState(globalContext.getLastState());
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
