package com.example.sigma_blue.fragments;

import android.content.res.Configuration;
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

import com.azeesoft.lib.colorpicker.ColorPickerDialog;
import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.tag.Tag;
import com.google.android.material.snackbar.Snackbar;

/**
 * Fragment for editing existing tags. //TODO merge into tag add fragment
 */
public class TagEditFragment extends Fragment {
    private GlobalContext globalContext;
    private EditText inputField;
    private Button backButton;
    private Button confirmButton;
    private Button colourButton;
    private Tag oldTag;
    private Tag modifiedTag;

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        inputField = view.findViewById(R.id.input_field);
        backButton = view.findViewById(R.id.back_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        colourButton = view.findViewById(R.id.color_button);

        globalContext = GlobalContext.getInstance();
        oldTag = globalContext.getSelectedTags().getSelected().get(0);
        modifiedTag = new Tag(oldTag.getTagText(), oldTag.getColour());

        colourButton.setBackgroundColor(oldTag.getColour().toArgb());
        inputField.setText(oldTag.getTagText());

        backButton.setOnClickListener(v -> {
            // exit fragment?
            getActivity().onBackPressed();
        });

        confirmButton.setOnClickListener(v -> {
            if (globalContext.getTagList().getEntityList().contains(modifiedTag) && !modifiedTag.equals(oldTag)) {
                Snackbar incorrectMessage = Snackbar.make(v, "Tag Already Exists", Snackbar.LENGTH_LONG);
                incorrectMessage.show();
            } else {
                globalContext.updateTag(modifiedTag, oldTag);
                globalContext.getSelectedTags().resetSelected();
                getActivity().onBackPressed();
            }
        });

        colourButton.setOnClickListener(v -> {
            this.pickColour();
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
                modifiedTag.setTagText(inputField.getText().toString());
                if (inputEmpty) {
                    confirmButton.setEnabled(false);
                } else {
                    confirmButton.setEnabled(true);
                }
            }
        });
    }

    private void pickColour() {
        ColorPickerDialog colorPickerDialog;
        int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        Boolean nightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
        if (nightMode) {
            colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this.getContext(),ColorPickerDialog.DARK_THEME);;
        }
        else {
            colorPickerDialog = ColorPickerDialog.createColorPickerDialog(this.getContext());
        }

        colorPickerDialog.setInitialColor(modifiedTag.getColour().toArgb());
        colorPickerDialog.setLastColor(modifiedTag.getColour().toArgb());
        colorPickerDialog.show();
        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String hexVal) {
                Color pickedColor = Color.valueOf(Color.parseColor(hexVal));
                colourButton.setBackgroundColor(Color.parseColor(hexVal));
                modifiedTag.setColour(pickedColor);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.edit_tag_fragment, container, false);
    }

}
