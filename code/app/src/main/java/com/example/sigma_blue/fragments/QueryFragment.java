package com.example.sigma_blue.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;

/**
 * Fragment class for the dialog fragment. Controls the UI element and
 * communicate with the backend.
 */
public class QueryFragment extends DialogFragment {
    private GlobalContext globalContext;    // Used for transferring data

    /**
     * ViewHolder design pattern for better encapsulation of the UI elements
     */
    private class ViewHolder {
        Button backButton, resetButton;
        EditText descriptionFilterET;
        Spinner sortCriteriaSpinner, tagFilterSpinner;
        CheckBox ascendingBox, descendingBox;
        DatePicker startDatePicker, endDatePicker;


        /**
         * Needs the parent view to be inflated before this class can be
         * constructed
         * @param entireView is the parent view (dialog box fragment)
         */
        public ViewHolder(View entireView) {
            bindViews(entireView);
            resetQuery();
        }

        /**
         * Method binds all the encapsulated views with the inflated layout.
         * @param entireView the view of the fragment
         */
        private void bindViews(View entireView) {
            backButton = entireView.findViewById(R.id.query_cancel_button);
            resetButton = entireView.findViewById(R.id.sortingResetButton);
            descriptionFilterET = entireView.findViewById(R.id
                    .descFilterEditText);
            sortCriteriaSpinner = entireView.findViewById(R.id
                    .sortCriteriaSpinner);
            tagFilterSpinner = entireView.findViewById(R.id.tagFilterSpinner);
            ascendingBox = entireView.findViewById(R.id.ascendCheckbox);
            descendingBox = entireView.findViewById(R.id.descendCheckbox);
            startDatePicker = entireView.findViewById(R.id.startDatePicker);
            endDatePicker = entireView.findViewById(R.id.endDatePicker);
        }

        /**
         * Flips between the check checkboxes
         * @param p when true will make checkbox ascending, with descending off.
         *          When false, the descending checkbox is selected and the
         *          ascending checkbox is off.
         */
        public void flipAscendBox(boolean p) {
            ascendingBox.setChecked(p);
            descendingBox.setChecked(!p);
        }

        /**
         * Resets the query. Everything is returned to default value, along with
         * the global state.
         */
        private void resetQuery() {
            globalContext.getItemList().startListening();
            sortCriteriaSpinner.setSelection(0);
            tagFilterSpinner.setSelection(0);
            flipAscendBox(true);
        }

        /**
         * Method sets up the UI interactions.
         */
        public void setUIListeners() {
            /* Closes the dialog fragment and return to the previous page */
            backButton.setOnClickListener(view -> dismiss());   // Go back

            /* Resets the query. Uses the database default */
            resetButton.setOnClickListener(view -> {
                resetQuery();
            });

            ascendingBox.setOnClickListener(view -> {
                flipAscendBox(true);    // Turns descend off
                globalContext.getQueryState().setAscend();
            });

            descendingBox.setOnClickListener(view -> {
                flipAscendBox(false);   // Turns ascend off
                globalContext.getQueryState().setDescend();
            });
        }
    }

    private ViewHolder viewHolder;          // The view holder

    /**
     * Empty public constructor
     */
    public QueryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = GlobalContext.getInstance();
    }

    /**
     * Override for the onCreateView. This lets us use the inflater that is
     * provided by android instead of making a new one.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstance If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstance) {
        View fragmentView = inflater.inflate(R.layout.sort_filter_fragment,
                container, false);

        viewHolder = new ViewHolder(fragmentView);

        viewHolder.setUIListeners();

        return fragmentView;
    }
}
