package com.example.sigma_blue.fragments;

import android.os.Bundle;
import android.text.Editable;
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
import com.example.sigma_blue.query.FilterField;
import com.example.sigma_blue.query.QueryMode;
import com.example.sigma_blue.query.SortField;
import com.example.sigma_blue.utility.Pair;
import com.example.sigma_blue.utility.SigmaBlueTextWatcher;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment class for the dialog fragment. Controls the UI element and
 * communicate with the backend.
 * TODO: Filter has not been implemented yet.
 */
public class QueryFragment extends DialogFragment {

    /**
     * ViewHolder design pattern for better encapsulation of the UI elements
     */
    private class ViewHolder {
        Button backButton, resetButton;
        EditText descriptionFilterET, makeFilterET;
        Spinner sortCriteriaSpinner, tagFilterSpinner;
        CheckBox ascendingBox, descendingBox;
        DatePicker startDatePicker, endDatePicker;
        ArrayAdapter<SortField> adapter;

        /**
         * Needs the parent view to be inflated before this class can be
         * constructed
         *
         * @param entireView is the parent view (dialog box fragment)
         */
        public ViewHolder(View entireView) {
            bindViews(entireView);
            setAdapters();
            resetQueryUI();
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
            makeFilterET = entireView.findViewById(R.id.makeFilterEditText);
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

        /**
         * Creates the menu items. Storing it in a list before placing it into
         * the adapter
         * @return the list of the possible options in the form of an enum.
         */
        private List<SortField> createMenuItems() {
            List<SortField> menuItems = new ArrayList<>();

            menuItems.add(SortField.NO_SELECTION);
            menuItems.add(SortField.NAME);
            menuItems.add(SortField.DATE);
            menuItems.add(SortField.MAKE);
            menuItems.add(SortField.VALUE);
            menuItems.add(SortField.DESCRIPTION);

            return menuItems;
        }

        /**
         * Creates the sorting choice adapter.
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
         * Resets the query UI. This just puts the UI at a known default state
         */
        private void resetQueryUI() {
            sortCriteriaSpinner.setSelection(0);
            tagFilterSpinner.setSelection(0);
            flipAscendBox(true);
        }

        /**
         * Actually clear the database query, and then resets the UI to be the
         * default value.
         */
        private void resetQuery() {
            queryState.clearQuery();
            regenerateSelection();
        }

        /**
         * Restores the selection that has been saved to the global context.
         */
        public void regenerateSelection() {
            sortCriteriaSpinner.setSelection(adapter.getPosition(queryState
                    .getCurrentSort()));
            setSortCheckbox(globalContext.getQueryState().getDirection());

            // Filter text regeneration
            regenerateMakeTextBox();
            regenerateDescriptionTextBox();
        }

        /**
         * This method returns the make edit textbox back to its initial state
         */
        private void regenerateMakeTextBox() {
            regenerateTextBox(makeFilterET, queryState
                    .getMakeFilter().getSecond());
        }

        /**
         * Regenerates the edit text for the description filter using the
         * previously stored state.
         */
        private void regenerateDescriptionTextBox() {
            regenerateTextBox(descriptionFilterET, queryState
                    .getDescriptionFilter().getSecond());
        }

        /**
         * Factored out method that contains the text setting logic
         * @param et the edit text box that is being changed
         * @param text the string input (from the control instance)
         */
        private void regenerateTextBox(EditText et, String text) {
            if (text != null) et.setText(text);
            else et.setText("");    // For real time feedback
        }

        /**
         * Match the UI with the direction that has been cached
         * @param direction is the query direction that the ui is being flipped
         *                  to.
         */
        private void setSortCheckbox(Query.Direction direction) {
            flipAscendBox(direction == Query.Direction.ASCENDING);
        }

        /**
         * Method sets up the UI interactions.
         */
        public void setUIListeners() {
            /* Closes the dialog fragment and return to the previous page */
            backButton.setOnClickListener(view -> {
                dismiss();
            });   // Go back

            /* Resets the query. Uses the database default */
            resetButton.setOnClickListener(view -> resetQuery());

            ascendingBox.setOnClickListener(view -> {
                flipAscendBox(true);    // Turns descend off
                globalContext.getQueryState().setAscend();
                globalContext.getQueryState().sendQuery(globalContext
                        .getQueryPair());
            });

            descendingBox.setOnClickListener(view -> {
                flipAscendBox(false);   // Turns ascend off
                globalContext.getQueryState().setDescend();
                globalContext.getQueryState().sendQuery(globalContext
                        .getQueryPair());
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

            makeFilterET.addTextChangedListener(
                    new SigmaBlueTextWatcher<EditText>(makeFilterET) {

                @Override
                public void onTextChanged(EditText target, Editable s) {
                    String userInput = s.toString().trim();
                    // Cover -> empty input, regular input
                    Pair<FilterField, String> nextAddition;
                    if (userInput.isEmpty())
                        nextAddition = new Pair<>(FilterField.MAKE, null);
                    else
                        nextAddition = new Pair<>(FilterField.MAKE, userInput);
                    queryState.receiveEqualsQuery(nextAddition);
                    queryState.sendQuery(globalContext.getQueryPair());
                }
            });

            descriptionFilterET.addTextChangedListener(
                    new SigmaBlueTextWatcher<EditText>(descriptionFilterET) {
                        @Override
                        public void onTextChanged(EditText target, Editable s) {
                            String userInput = s.toString().trim();
                            // Cover -> empty input, regular input
                            Pair<FilterField, String> nextAddition;
                            if (userInput.isEmpty())
                                nextAddition = new Pair<>(FilterField
                                        .DESCRIPTION, null);
                            else
                                nextAddition = new Pair<>(FilterField
                                        .DESCRIPTION, userInput);
                            queryState.receiveEqualsQuery(nextAddition);
                            queryState.sendQuery(globalContext.getQueryPair());
                        }
                    });
        }
    }

    private GlobalContext globalContext;    // Used for transferring data
    private ViewHolder viewHolder;          // The view holder
    private QueryMode queryState;           // The query controller.

    /**
     * Empty public constructor
     */
    public QueryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = GlobalContext.getInstance();
        queryState = globalContext.getQueryState();
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
     * @return the generated view
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
