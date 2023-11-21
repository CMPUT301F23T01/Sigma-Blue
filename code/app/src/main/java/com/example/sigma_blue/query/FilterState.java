package com.example.sigma_blue.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class controls what filter state the list is currently in. Used to
 * connect the GUI with the backend, and lets the application know the state
 * that it is in.
 */
public class FilterState {
    private final List<FilterField> appliedFilters;

    public FilterState() {
        appliedFilters = new ArrayList<>();
    }

    /**
     * Adding a new filter state.
     * @param newField is the new filter type that is being applied
     */
    public void addFilter(final FilterField newField) {
        if (Objects.requireNonNull(newField) == FilterField.NONE) { // Use reset
            throw new IllegalArgumentException("NONE is not a valid argument");
        } else if (appliedFilters.contains(newField)) {
            throw new IllegalArgumentException("Field already in filter state");
        } else {
            appliedFilters.add(newField);
        }
    }

    /**
     * Clear the filter state, bringing it back into no filter
     */
    public void resetState() {
        appliedFilters.clear();
        appliedFilters.add(FilterField.NONE);
    }

    /**
     * When the state needs to be parsed
     * @return returns the filter for parsing
     */
    public List<FilterField> getAppliedFilters() {
        return this.appliedFilters;
    }
}
