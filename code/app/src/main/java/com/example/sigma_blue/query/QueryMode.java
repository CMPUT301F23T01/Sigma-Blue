package com.example.sigma_blue.query;

import com.google.firebase.firestore.Query;

/**
 * This class will store the current query mode that the list view is in.
 * Needed for clean control of the query view UI.
 * TODO: Put in the global context
 */
public class QueryMode {
    Boolean sort, filter;           // Should be able to be in four diff states
    SortField currentSort;          // What is currently being sorted.
    Query.Direction direction;      // Defaults to ASCENDING
    final FilterState filterState;  // Keeping track of the applied filters
    private QueryGenerator queryFactory;    // Generation of new queries

    public QueryMode() {
        // Initialization
        filterState = new FilterState();

        // Set basic state
        clearQuery();
    }

    /**
     * Reset the query mode back to default
     */
    public void clearQuery() {
        currentSort = SortField.NAME;
        filterState.resetState();
        direction = Query.Direction.ASCENDING;  // Default sort direction
        sort = false;
        filter = false;
    }

    public void sortOn() {
        sort = true;
    }

    public void sortOff() {
        sort = false;
    }

    public void filterOn(FilterState filterState) {
        filter = true;
    }

    /**
     * Get rid of all filter options
     */
    public void filterOff() {
        filter = false;
        filterState.resetState();
    }

    /**
     * Flips the switch to make the sorting be ascending
     */
    public void setAscend() {
        direction = Query.Direction.ASCENDING;
    }

    /**
     * Flips teh switch to make the sort be descending
     */
    public void setDescend() {
        direction = Query.Direction.DESCENDING;
    }
}
