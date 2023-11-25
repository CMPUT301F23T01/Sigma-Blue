package com.example.sigma_blue.query;

import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.utility.Pair;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * This class will store the current query mode that the list view is in.
 * Needed for clean control of the query view UI.
 * TODO: Put in the global context
 */
public class QueryMode {
    private SortField currentSort;          // What is currently being sorted.
    private Query.Direction direction;      // Defaults to ASCENDING
    private Query currentQuery;
    private CollectionReference originalQuery;
    private Pair<FilterField, String> makeFilter;

    public QueryMode(CollectionReference originalQuery) {
        // Initialization
        this.originalQuery = originalQuery; // Base reference for query

        // Set basic state
        clearQuery();
    }

    /**
     * Returns the direction that is saved into query mode
     * @return the Direction enum for the current query
     */
    public Query.Direction getDirection() {
        return this.direction;
    }

    public SortField getCurrentSort() {
        return this.currentSort;
    }

    /**
     * Reset the query mode back to default. It removes all the user settings.
     */
    public void clearQuery() {
        currentSort = SortField.NO_SELECTION;
        direction = Query.Direction.ASCENDING;  // Default sort direction
        currentQuery = originalQuery;
        makeFilter = new Pair<>(FilterField.MAKE, null);
    }

    /**
     * Simply resets just the query object to the base. Does not reset the user
     * setting
     */
    public void resetQueryObject() {
        currentQuery = originalQuery;
    }

    /**
     * Receives the sort mode and then store the appropriate query
     * @param sortMode
     */
    public void receiveSortQuery(SortField sortMode) {
        if (sortMode != null) currentSort = sortMode;
        queryUpdate();
    }

    /**
     * This method receives the user input data for an equals type query, and
     * then change the internal state of the instance to match.
     * @param input is the pair value that is being used to control flow.
     */
    public void receiveEqualsQuery(Pair<FilterField, String> input) {
        switch(input.getFirst()) {
            case MAKE:
                makeFilter = input;
                break;
            case DESCRIPTION:
                break;  // TODO: Implement filter by description
            default:
                throw new IllegalArgumentException(
                        "Filter communications receiving wrong format");
        }
        queryUpdate();
    }

    /**
     * This method updates the internally stored Query object to match with the
     * currently toggle front end.
     */
    public void queryUpdate() {
        resetQueryObject();   // Need to reset before running

        updateMakeFilter();

        if (currentSort != SortField.NO_SELECTION) {
            currentQuery = QueryGenerator.sortQuery(currentQuery, currentSort,
                    direction);
        }
    }

    /**
     * This method updates the internal state of the make filter query. Done by
     * reading the internal state as well.
     */
    private void updateMakeFilter() {
        if (makeFilter.getSecond() != null) compoundQuery(q ->
                QueryGenerator.filterEqualsQuery(q, makeFilter.getFirst()
                        .getDbField(), makeFilter.getSecond()));

    }

    /**
     * This method sends the query to the database for display. Use after every
     * query update.
     */
    public void sendQuery(Pair<ADatabaseHandler<Item>, IDatabaseList<Item>>
                                  dbPair) {
        dbPair.getFirst().startListening(currentQuery, dbPair.getSecond());
    }

    /**
     * State mutator that takes a lambda as an input
     * @param composer is the function that will generate the next query state.
     */
    private void compoundQuery(Function<Query, Query> composer) {
        currentQuery = composer.apply(currentQuery);
    }

    public Pair<FilterField, String> getMakeFilter() {
        return this.makeFilter;
    }

    /**
     * Flips the switch to make the sorting be ascending
     */
    public void setAscend() {
        direction = Query.Direction.ASCENDING;
        queryUpdate();
    }

    /**
     * Flips teh switch to make the sort be descending
     */
    public void setDescend() {
        direction = Query.Direction.DESCENDING;
        queryUpdate();
    }
}
