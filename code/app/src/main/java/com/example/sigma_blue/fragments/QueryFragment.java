package com.example.sigma_blue.fragments;

import static com.example.sigma_blue.utility.ModeField.FILTER;
import static com.example.sigma_blue.utility.ModeField.SORT;

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
import com.example.sigma_blue.context.ApplicationState;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.item.VisibleItemList;
import com.example.sigma_blue.utility.DateFilterField;
import com.example.sigma_blue.utility.DescriptionFilterField;
import com.example.sigma_blue.utility.FilterField;
import com.example.sigma_blue.utility.ItemSortComparator;
import com.example.sigma_blue.utility.MakeFilterField;
import com.example.sigma_blue.utility.ModeField;
import com.example.sigma_blue.utility.NameFilterField;
import com.example.sigma_blue.utility.SearchTextBoxWatcher;
import com.example.sigma_blue.utility.SortField;
import com.google.firebase.firestore.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Fragment class for the dialog fragment. Controls the UI element and
 * communicate with the backend.
 */
public class QueryFragment extends DialogFragment {

    /**
     * ViewHolder design pattern for better encapsulation of the UI elements
     */
    private class ViewHolder {
        Button confirmButton, resetButton;
        TextView startDateTV, endDateTV;
        EditText descriptionFilterET, makeFilterET, nameFilterET;
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
         * @param view is the parent view (dialog box fragment)
         */
        public ViewHolder(View view) {
            bindViews(view);
            setAdapters();
            resetQueryUI();
            regenerateSelection();
        }

        /**
         * Method binds all the encapsulated views with the inflated layout.
         * @param view the view of the fragment
         */
        private void bindViews(View view) {
            confirmButton = view.findViewById(R.id.query_confirm_button);
            resetButton = view.findViewById(R.id.sortingResetButton);
            nameFilterET = view.findViewById(R.id.nameFilterEditText);
            descriptionFilterET = view.findViewById(R.id.descFilterEditText);
            makeFilterET = view.findViewById(R.id.makeFilterEditText);
            sortCriteriaSpinner = view.findViewById(R.id.sortCriteriaSpinner);
            tagFilterSpinner = view.findViewById(R.id.tagFilterSpinner);
            modeChoiceSpinner = view.findViewById(R.id.mode_choice_spinner);
            ascendingBox = view.findViewById(R.id.ascendCheckbox);
            descendingBox = view.findViewById(R.id.descendCheckbox);
            dateFilterBox = view.findViewById(R.id.dateFilterCheckbox);

            startDatePicker = view.findViewById(R.id.startDatePicker);
            endDatePicker = view.findViewById(R.id.endDatePicker);

            modeSwitcher = view.findViewById(R.id.query_view_switcher);

            startDateTV = view.findViewById(R.id.startDateTitle);
            endDateTV = view.findViewById(R.id.endDateTitle);
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
            visibleItemList.resetVisibleItems();
            globalContext.getItemList().updateUI();
            regenerateSelection();
        }

        /**
         * Restores the selection that has been saved to the global context.
         */
        public void regenerateSelection() {
            sortCriteriaSpinner.setSelection(sortAdapter.getPosition(visibleItemList.getItemSortComparator().getSortBy()));
            flipAscendBox((visibleItemList.getItemSortComparator().getDirection()) == 1);

            // Filter text regeneration
            regenerateTextBox(makeFilterET, visibleItemList.getMakeFilterField().getFilterText());
            regenerateTextBox(descriptionFilterET, visibleItemList.getDescriptionFilterField().getFilterText());
            regenerateTextBox(nameFilterET, visibleItemList.getNameFilterField().getFilterText());
            regenerateDateCheckBox();
            regenerateDatePickers();

            dateCheckBoxController(this.dateFilterBox);
        }

        /**
         * Factored out method that contains the text setting logic
         * @param et the edit text box that is being changed
         * @param text the string input (from the control instance)
         */
        private void regenerateTextBox(EditText et, String text) {
            if (text != null) {
                et.setText(text);
            }
            else {
                et.setText("");    // For real time feedback
            }
        }

        private void regenerateDatePickers() {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();

            if (visibleItemList.getDateFilterField().isEnabled()) {
                Date startDate = ((DateFilterField) visibleItemList.getDateFilterField()).getStartDate();
                Date endDate = ((DateFilterField) visibleItemList.getDateFilterField()).getEndDate();
                start.setTime(startDate);
                end.setTime(endDate);
            } else {
                // from the last week by default
                start.add(Calendar.DATE, -5);
            }

            startDatePicker.updateDate(
                    start.get(Calendar.YEAR),
                    start.get(Calendar.MONTH) - 1,
                    start.get(Calendar.DAY_OF_MONTH)
            );

            endDatePicker.updateDate(
                    end.get(Calendar.YEAR),
                    end.get(Calendar.MONTH) - 1,
                    end.get(Calendar.DAY_OF_MONTH)
            );
        }

        private void regenerateDateCheckBox() {
            dateFilterBox.setChecked(visibleItemList.getDateFilterField().isEnabled());
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

        private Date dateRepresentationOfPicker(DatePicker picker) {
            Calendar cal = Calendar.getInstance();
            cal.set(picker.getYear(), picker.getMonth() + 1, picker.getDayOfMonth());
            return cal.getTime();
        }
        /**
         * Creating reactions to checkbox interactions
         */
        private void setBoxListeners() {
            ascendingBox.setOnClickListener(view -> {
                flipAscendBox(true);    // Turns descend off
                visibleItemList.getItemSortComparator().setDirection(1);
            });

            descendingBox.setOnClickListener(view -> {
                flipAscendBox(false);   // Turns ascend off
                visibleItemList.getItemSortComparator().setDirection(-1);
            });

            dateFilterBox.setOnClickListener(view -> {
                // Need to show the element
                CheckBox datebox = (CheckBox) view;
                dateCheckBoxController(datebox);

                // Need to send the filter
                Date startDate = dateRepresentationOfPicker(startDatePicker);
                Date endDate = dateRepresentationOfPicker(endDatePicker);

                DateFilterField newDateFilter = new DateFilterField(startDate, endDate, true);
                visibleItemList.setDateFilterField(newDateFilter);
            });
        }

        private void setDatePickerListeners() {
            startDatePicker.setOnDateChangedListener((view, year, month, day) ->
            {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month + 1, day);
                Date startDate = cal.getTime();
                Date endDate = dateRepresentationOfPicker(endDatePicker);

                if (dateFilterBox.isChecked()) {
                    DateFilterField dateFilterField = new DateFilterField(startDate, endDate, true);
                    visibleItemList.setDateFilterField(dateFilterField);
                    //Log.d("DATE REP", startDate.toString());
                } else {
                    throw new IllegalStateException(
                            "Impossible start date picker state");
                }
            });
            endDatePicker.setOnDateChangedListener((view, year, month, day) ->
            {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month + 1, day);
                Date startDate = dateRepresentationOfPicker(startDatePicker);
                Date endDate = cal.getTime();

                if (dateFilterBox.isChecked()) {
                    DateFilterField dateFilterField = new DateFilterField(startDate, endDate, true);
                    visibleItemList.setDateFilterField(dateFilterField);
                    //Log.d("DATE REP", startDate.toString());
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
            confirmButton.setOnClickListener(view -> {
                globalContext.getItemList().updateUI();
                globalContext.newState(globalContext.getLastState());
                dismiss();
            });   // Go back

            /* Resets the query. Uses the database default */
            resetButton.setOnClickListener(view -> {
                resetQuery();
                globalContext.newState(globalContext.getLastState());
                dismiss();
            });

            setBoxListeners();  // Checkbox UI listeners
            setDatePickerListeners(); // date picker listeners

            sortCriteriaSpinner.setOnItemSelectedListener(new AdapterView
                    .OnItemSelectedListener() {

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
                    handleSortUpdate(position);
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
                    new SearchTextBoxWatcher<EditText>(makeFilterET) {

                @Override
                public void onTextChanged(EditText target, Editable s) {
                    String userInput = s.toString().trim();
                    // Cover -> empty input, regular input
                    MakeFilterField nextAddition;
                    if (userInput.isEmpty()) {
                        nextAddition = new MakeFilterField(null, false, false);
                    } else {
                        nextAddition = new MakeFilterField(userInput, true, false);
                    }
                    visibleItemList.setMakeFilterField(nextAddition);
                }
            });

            descriptionFilterET.addTextChangedListener(
                    new SearchTextBoxWatcher<EditText>(descriptionFilterET) {
                        @Override
                        public void onTextChanged(EditText target, Editable s) {
                            String userInput = s.toString().trim();
                            // Cover -> empty input, regular input
                            DescriptionFilterField nextAddition;
                            if (userInput.isEmpty()) {
                                nextAddition = new DescriptionFilterField(null, false, false);
                            } else {
                                nextAddition = new DescriptionFilterField(userInput, true, false);
                            }
                            visibleItemList.setDescriptionFilterField(nextAddition);
                        }
                    });
            nameFilterET.addTextChangedListener(
                    new SearchTextBoxWatcher<EditText>(nameFilterET) {
                        @Override
                        public void onTextChanged(EditText target, Editable s) {
                            String userInput = s.toString().trim();
                            // Cover -> empty input, regular input
                            NameFilterField nextAddition;
                            if (userInput.isEmpty()) {
                                nextAddition = new NameFilterField(null, false, false);
                            } else {
                                nextAddition = new NameFilterField(userInput, true, false);
                            }
                            visibleItemList.setNameFilterField(nextAddition);
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

        private void handleSortUpdate(int position) {
            int d = visibleItemList.getItemSortComparator().getDirection();
            switch (position){
                case 0:
                    visibleItemList.setItemSortComparator(new ItemSortComparator(SortField.NAME, d));
                    break;
                case 1:
                    visibleItemList.setItemSortComparator(new ItemSortComparator(SortField.DATE, d));
                    break;
                case 2:
                    visibleItemList.setItemSortComparator(new ItemSortComparator(SortField.MAKE, d));
                    break;
                case 3:
                    visibleItemList.setItemSortComparator(new ItemSortComparator(SortField.VALUE, d));
                    break;
                case 4:
                    visibleItemList.setItemSortComparator(new ItemSortComparator(SortField.DESCRIPTION, d));
                    break;
            }
        }
    }

    private GlobalContext globalContext;    // Used for transferring data
    private ViewHolder viewHolder;          // The view holder
    private ModeField currentView;          // For state matching
    private VisibleItemList visibleItemList;

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
        visibleItemList = globalContext.getItemList().getVisibleItemList();
        currentView = SORT;
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

        if (globalContext.getCurrentState() == ApplicationState.SORT_MENU) {
            viewHolder.modeChoiceSpinner.setSelection(0);
        } else if (globalContext.getCurrentState() == ApplicationState.FILTER_MENU) {
            viewHolder.modeChoiceSpinner.setSelection(1);
        }

        return fragmentView;
    }
}