package com.example.sigma_blue.utility;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

/**
 * A specialized TextWatcher used for the filter/search fragment editText boxes.
 * @param <T> The type of text view to watch
 */
public abstract class SearchTextBoxWatcher<T extends TextView> implements TextWatcher {
    T target;   // The edit text that is being changed.

    /**
     * Constructor that takes the view that
     * @param target is the editable text view that will have an action after
     *               being edited
     */
    public SearchTextBoxWatcher(T target) {
        this.target = target;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        this.onTextChanged(this.target, s);
    }

    /**
     * This is the method that has to be implemented for the interface to be
     * working.
     * @param target is the editable box that is being watched
     * @param s is the string that the user input.
     */
    abstract public void onTextChanged(T target, Editable s);
}
