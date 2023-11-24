package com.example.sigma_blue.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.query.SortField;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Fragment class for the dialog fragment. Controls the UI element and
 * communicate with the backend.
 */
public class QueryFragment extends DialogFragment implements
        AdapterView.OnItemSelectedListener {
    private GlobalContext globalContext;    // Used for transferring data

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * ViewHolder design pattern for better encapsulation of the UI elements
     */
    private class ViewHolder {
        Button backButton, resetButton;
        EditText descriptionFilterET;
        Spinner sortCriteriaSpinner, tagFilterSpinner;
        CheckBox ascendingBox, descendingBox;
        DatePicker startDatePicker, endDatePicker;
        ArrayAdapter<SortField> adapter;


        /**
         * Needs the parent view to be inflated before this class can be
         * constructed
         * @param entireView is the parent view (dialog box fragment)
         */
        public ViewHolder(View entireView) {
            bindViews(entireView);
            setAdapters();
            resetQuery();
            regenerateSelection();
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
         * Sets the adapter for the selectable ui elements
         */
        private void setAdapters() {
            createSortAdapter();
            sortCriteriaSpinner.setAdapter(this.adapter);
        }

        private List<SortField> createMenuItems() {
            List<SortField> menuItems = new ArrayList<>();

            menuItems.add(SortField.NO_SELECTION);
            menuItems.add(SortField.DATE);
            menuItems.add(SortField.MAKE);
            menuItems.add(SortField.VALUE);
            menuItems.add(SortField.DESCRIPTION);

            return menuItems;
        }

        /**
         * This
         */
        private void createSortAdapter() {
            adapter = new ArrayAdapter<>(getContext(), android.R.layout
                    .simple_spinner_dropdown_item);
            adapter.addAll(createMenuItems());
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
            sortCriteriaSpinner.setSelection(0);
            tagFilterSpinner.setSelection(0);
            flipAscendBox(true);
        }

        public void regenerateSelection() {
            sortCriteriaSpinner.setSelection(adapter.getPosition(globalContext
                    .getQueryState().getCurrentSort()));
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

            // TODO: The performance seems bad. Feels slow to load. Might have
            // to factor some methods and components out to reuse some objects
            sortCriteriaSpinner.setOnItemSelectedListener(new AdapterView
                    .OnItemSelectedListener() {
                // There are two methods, therefore need to make anonymous class
                // instead of lambda

                /**
                 * This method controls the state when an item is selected by
                 * the user on the sort choice spinner.
                 * @param parent The AdapterView where the selection happened
                 * @param view The view within the AdapterView that was clicked
                 * @param position The position of the view in the adapter
                 * @param id The row id of the item that is selected
                 */
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    globalContext.getQueryState().receiveSortQuery(adapter
                            .getItem(position));    // Update for return

                    /* Sending the query to the database */
                    globalContext.getQueryState().sendQuery(globalContext
                            .getQueryPair());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Does not need to do anything yet
                }
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
