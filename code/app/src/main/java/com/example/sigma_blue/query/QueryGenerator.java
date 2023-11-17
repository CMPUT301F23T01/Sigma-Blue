package com.example.sigma_blue.query;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

public class QueryGenerator {
    private Query query;

    /**
     * Case where sorting only
     * @param sortBy the field that the sort is happening by
     * @param direction ASCENDING or DESCENDING
     * @param cr the collection reference that is being used (Item only for now)
     */
    public QueryGenerator(SortField sortBy, Query.Direction direction,
                          CollectionReference cr) {
        query = cr.orderBy(sortBy.getDbField(), direction);
    }

    public Query get() {
        return this.query;
    }
}
