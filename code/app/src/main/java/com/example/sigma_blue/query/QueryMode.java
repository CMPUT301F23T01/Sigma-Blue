package com.example.sigma_blue.query;

/**
 * This class will store the current query mode that the list view is in.
 * TODO: Put in the global context
 */
public class QueryMode {
    SortField currentSort;
    final FilterState filterState;    // Keeping track of the applied filters

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
    }
}
