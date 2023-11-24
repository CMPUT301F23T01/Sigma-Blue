package com.example.sigma_blue.query;

import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.IDatabaseItem;
import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.utility.Pair;
import com.google.firebase.firestore.CollectionReference;
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
    private Query currentQuery;
    private CollectionReference originalQuery;

    public QueryMode(CollectionReference originalQuery) {
        // Initialization
        filterState = new FilterState();
        this.originalQuery = originalQuery; // Base reference for query

        // Set basic state
        clearQuery();
    }

    public SortField getCurrentSort() {
        return this.currentSort;
    }

    /**
     * Reset the query mode back to default
     */
    public void clearQuery() {
        currentSort = SortField.NO_SELECTION;
        filterState.resetState();
        direction = Query.Direction.ASCENDING;  // Default sort direction
        sort = false;
        filter = false;
        currentQuery = originalQuery;
    }

    /**
     * Receives the sort mode and then store the appropriate query
     * @param sortMode
     */
    public void receiveSortQuery(SortField sortMode) {
        if (sortMode != null) currentSort = sortMode;
        queryUpdateSort();
    }

    public void queryUpdateSort() {
        if (currentSort != SortField.NO_SELECTION) {
            currentQuery = QueryGenerator.sortQuery(currentQuery, currentSort,
                    direction);
        }
    }

    /**
     * This method sends the query to the database for display. Use after every
     * query update.
     */
    public void sendQuery(Pair<ADatabaseHandler<Item>, IDatabaseList<Item>>
                                  dbPair) {
        dbPair.getFirst().startListening(currentQuery, dbPair.getSecond());
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
