package com.example.sigma_blue.fragments;

import static com.example.sigma_blue.query.ModeField.FILTER;
import static com.example.sigma_blue.query.ModeField.SORT;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.fragment.app.DialogFragment;

import com.example.sigma_blue.R;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.query.DateRepresentation;
import com.example.sigma_blue.query.FilterField;
import com.example.sigma_blue.query.ModeField;
import com.example.sigma_blue.query.QueryMode;
import com.example.sigma_blue.query.SortField;
import com.example.sigma_blue.utility.Pair;
import com.example.sigma_blue.utility.Quadruple;
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
        TextView startDateTV, endDateTV;
        EditText descriptionFilterET, makeFilterET;
        Spinner sortCriteriaSpinner, tagFilterSpinner, modeChoiceSpinner;
        CheckBox ascendingBox, descendingBox, dateFilterBox;
        DatePicker startDatePicker, endDatePicker;
        ArrayAdapter<SortField> sortAdapter;
        ArrayAdapter<ModeField> modeAdapter;
        ViewSwitcher modeSwitcher;

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
            backButton = entireView.findViewById(R.id.query_confirm_button);
            resetButton = entireView.findViewById(R.id.sortingResetButton);
            descriptionFilterET = entireView.findViewById(R.id
                    .descFilterEditText);
            makeFilterET = entireView.findViewById(R.id.makeFilterEditText);
            sortCriteriaSpinner = entireView.findViewById(R.id
                    .sortCriteriaSpinner);
            tagFilterSpinner = entireView.findViewById(R.id.tagFilterSpinner);
            modeChoiceSpinner = entireView.findViewById(R.id
                    .mode_choice_spinner);
            ascendingBox = entireView.findViewById(R.id.ascendCheckbox);
            descendingBox = entireView.findViewById(R.id.descendCheckbox);
            dateFilterBox = entireView.findViewById(R.id.dateFilterCheckbox);

            startDatePicker = entireView.findViewById(R.id.startDatePicker);
            endDatePicker = entireView.findViewById(R.id.endDatePicker);

            modeSwitcher = entireView.findViewById(R.id.query_view_switcher);

            startDateTV = entireView.findViewById(R.id.startDateTitle);
            endDateTV = entireView.findViewById(R.id.endDateTitle);
        }

        /**
         * Sets the adapter for the selectable ui elements
         */
        private void setAdapters() {
            createSortAdapter();
            createModeAdapter();

            /* Binding the adapters */
            sortCriteriaSpinner.setAdapter(this.sortAdapter);
            modeChoiceSpinner.setAdapter(this.modeAdapter);
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
         * Generates the content of the mode menu items
         * @return a list of menu items
         */
        private List<ModeField> createModeChoices() {
            List<ModeField> ret = new ArrayList<>();

            ret.add(SORT);
            ret.add(FILTER);

            return ret;
        }

        /**
         * Creates the sorting choice adapter.
         */
        private void createSortAdapter() {
            // TODO: There is reuse of this function, therefore, it can be refac
            sortAdapter = new ArrayAdapter<>(getContext(), android.R.layout
                    .simple_spinner_dropdown_item);
            sortAdapter.addAll(createMenuItems());
        }

        /**
         * Populating the adapter mode menu items.
         */
        private void createModeAdapter() {
            modeAdapter = new ArrayAdapter<>(getContext(), android.R.layout
                    .simple_spinner_dropdown_item);
            modeAdapter.addAll(createModeChoices());
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
            queryState.sendQuery(globalContext.getQueryPair());
            regenerateSelection();
        }

        /**
         * Restores the selection that has been saved to the global context.
         */
        public void regenerateSelection() {
            sortCriteriaSpinner.setSelection(sortAdapter.getPosition(queryState
                    .getCurrentSort()));
            setSortCheckbox(globalContext.getQueryState().getDirection());

            // Filter text regeneration
            regenerateMakeTextBox();
            regenerateDescriptionTextBox(queryState);
            regenerateDateUI(queryState);

            dateCheckBoxController(this.dateFilterBox);
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
        private void regenerateDescriptionTextBox(QueryMode queryState) {
            regenerateTextBox(descriptionFilterET, queryState
                    .getDescriptionFilter().getSecond());
        }

        /**
         * Method that syncs the fragment ui with what is stored in the global
         * context.
         * @param queryMode is the controller class for the queries
         */
        private void regenerateDateUI(QueryMode queryMode) {
            regenerateDateCheckBox(queryMode);
            regenerateDatePickers(queryMode.getDateFilter());
        }

        private void regenerateDatePickers(Quadruple<FilterField, Boolean,
                DateRepresentation, DateRepresentation> dateFilterState) {
            DateRepresentation start;
            DateRepresentation end;
            if (dateFilterState.getSecond()) {
                // TODO: Refactor this section of code
                start = dateFilterState.getThird();
                end = dateFilterState.getFourth();
                startDatePicker.updateDate(start.getYear(), start.getMonth(),
                        start.getDay());
                endDatePicker.updateDate(end.getYear(), end.getMonth(),
                        end.getDay());
            }
        }

        private void regenerateDateCheckBox(QueryMode queryMode) {
            Quadruple<FilterField, Boolean, DateRepresentation,
                    DateRepresentation> cache = queryMode.getDateFilter();
            dateFilterBox.setChecked(cache.getSecond());
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
         * Sets the visibility of the date pickers programmically.
         * @param mode if true, then the date picker elements will be visible,
         *             else it will be gone.
         */
        private void setDatePickerVisibility(int mode) {
            startDateTV.setVisibility(mode);
            startDatePicker.setVisibility(mode);

            endDateTV.setVisibility(mode);
            endDatePicker.setVisibility(mode);
        }

        /**
         * Checks if the input CheckBox is checked or not. If checked, then
         * the date picker elements are visible, else invisible
         * @param dateBox is the checkbox being used for control
         */
        private void dateCheckBoxController(CheckBox dateBox) {
            if (dateBox.isChecked()) setDatePickerVisibility(View.VISIBLE);
            else setDatePickerVisibility(View.GONE);
        }

        private DateRepresentation dateRepresentationOfPicker(
                DatePicker picker) {
            return new DateRepresentation(picker.getYear(), picker.getMonth(),
                    picker.getDayOfMonth());
        }

        /**
         * Creating reactions to checkbox interactions
         */
        private void setBoxListeners() {
            ascendingBox.setOnClickListener(view -> {
                flipAscendBox(true);    // Turns descend off
                queryState.setAscend();
            });

            descendingBox.setOnClickListener(view -> {
                flipAscendBox(false);   // Turns ascend off
                queryState.setDescend();
            });

            dateFilterBox.setOnClickListener(view -> {
                // Need to show the element
                CheckBox datebox = (CheckBox) view;
                dateCheckBoxController(datebox);

                // Need to send the filter
                dateFilter = datebox.isChecked();
                if (dateFilter) {
                    Quadruple<FilterField, Boolean, DateRepresentation,
                            DateRepresentation> parcel;
                    // TODO: Factor this out into a method
                    DateRepresentation startDate = dateRepresentationOfPicker(
                            startDatePicker);
                    DateRepresentation endDate = dateRepresentationOfPicker(
                            endDatePicker);
                    parcel = new Quadruple<>(FilterField.DATE_RANGE, dateFilter, startDate, endDate);
                    queryState.receiveQuery(parcel);
                }
            });
        }

        private void setDatePickerListeners() {
            startDatePicker.setOnDateChangedListener((view, year, month, day) ->
            {
                Quadruple<FilterField, Boolean, DateRepresentation,
                        DateRepresentation> parcel;
                DateRepresentation startDate = new DateRepresentation(year,
                        month, day);
                DateRepresentation endDate = dateRepresentationOfPicker(
                        endDatePicker);

                if (dateFilterBox.isChecked()) {
                    parcel = new Quadruple<>(FilterField.DATE_RANGE, true, startDate, endDate);
                    queryState.receiveQuery(parcel);
                    Log.d("DATE REP", startDate.toString());
                } else {
                    throw new IllegalStateException(
                            "Impossible start date picker state");
                }
            });

            endDatePicker.setOnDateChangedListener((view, year, month, day) ->
            {
                Quadruple<FilterField, Boolean, DateRepresentation,
                        DateRepresentation> parcel;
                DateRepresentation startDate = dateRepresentationOfPicker(
                        startDatePicker);
                DateRepresentation endDate = new DateRepresentation(year, month,
                        day);

                // TODO: this next few lines can be factored out into a method
                if (dateFilterBox.isChecked()) {
                    parcel = new Quadruple<>(FilterField.DATE_RANGE, true, startDate, endDate);
                    queryState.receiveQuery(parcel);
                    Log.d("DATE REP", startDate.toString());
                } else {
                    throw new IllegalStateException(
                            "Impossible start date picker state");
                }
            });
        }

        /**
         * Method sets up the UI interactions.
         */
        public void setUIListeners() {
            /* Closes the dialog fragment and return to the previous page */
            backButton.setOnClickListener(view -> {
                queryState.sendQuery(globalContext.getQueryPair());
                dismiss();
            });   // Go back

            /* Resets the query. Uses the database default */
            resetButton.setOnClickListener(view -> resetQuery());

            setBoxListeners();  // Checkbox UI listeners
            setDatePickerListeners();

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
                    queryState.receiveQuery(sortAdapter.getItem(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Does not need to do anything yet
                }
            });

            modeChoiceSpinner.setOnItemSelectedListener(new AdapterView
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
                    chooseModeView(modeAdapter.getItem(position));
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
                    // TODO: Refactor opportunity
                    String userInput = s.toString().trim();
                    // Cover -> empty input, regular input
                    Pair<FilterField, String> nextAddition;
                    if (userInput.isEmpty())
                        nextAddition = new Pair<>(FilterField.MAKE, null);
                    else
                        nextAddition = new Pair<>(FilterField.MAKE, userInput);
                    queryState.receiveQuery(nextAddition);
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
                            queryState.receiveQuery(nextAddition);
                        }
                    });
        }


        private void chooseModeView(ModeField choice) {
            switch(choice) {
                case SORT:
                    swapMode(SORT);
                    break;
                case FILTER:
                    swapMode(FILTER);
                    break;
                default:
                    throw new IllegalStateException(
                            "Query Mode Menu Illegal state");
            }
        }

        private void swapMode(ModeField mode) {
            if (currentView != mode) {
                modeSwitcher.showNext();
                currentView = mode;
            }
        }
    }

    private GlobalContext globalContext;    // Used for transferring data
    private ViewHolder viewHolder;          // The view holder
    private QueryMode queryState;           // The query controller.
    private ModeField currentView;          // For state matching
    private Boolean dateFilter;

    /**
     * Empty public constructor
     */
    public QueryFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        // Adjusting the size of the dialog fragment
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        if (params != null) {
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setAttributes(
                    (WindowManager.LayoutParams) params);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globalContext = GlobalContext.getInstance();
        queryState = globalContext.getQueryState();
        currentView = SORT;
        dateFilter = false;
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
