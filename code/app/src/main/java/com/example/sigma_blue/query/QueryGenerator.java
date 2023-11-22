package com.example.sigma_blue.query;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;

import java.util.List;

public class QueryGenerator {
    private Query query;

    /**
     * Runs sorting based on the field enum and the direction.
     * @param query the query pointer being used as the base
     * @param sortBy the field that is being used for sorting
     * @param direction either ASCENDING or DESCENDING
     * @return a new query object with the query applied
     */
    public static Query sortQuery(Query query, SortField sortBy,
                                  Query.Direction direction) {
        return query.orderBy(sortBy.getDbField(), direction);
    }

    /**
     * This method will return a new query object with an equals filter applied
     * to it.
     * @param query is the query object that the filtering is being applied to.
     *              This object can either be the original collection reference,
     *              or it could be another query (that has already been
     *              queried). This method is likely being used most with the
     *              search by make.
     * @param field This is the document field that is being matched
     * @param keyword This is the keyword being used for filtering
     * @return a new Query object that has been filtered to specification
     */
    public static Query filterEqualsQuery(Query query, String field,
                                          String keyword) {
        return query.whereEqualTo(field, keyword);
    }

    /**
     * This method will return a query object that only contains the items
     * between the two bounds
     * @param query query object
     * @param field is the field being compared with
     * @param lowerRange is the lower bound that will be included
     * @param upperRange is the upper bound that will be included
     * @return a new query object
     */
    public static Query filterRangeQuery(Query query, String field,
                                         String lowerRange, String upperRange) {
        return query.whereLessThanOrEqualTo(field, upperRange)
                .whereGreaterThanOrEqualTo(field, lowerRange);
    }

    public static Query filterInclusiveQuery(Query query, String field,
                                             List<Object> inclusionList) {
        return query.whereIn(field, inclusionList);
    }
}
