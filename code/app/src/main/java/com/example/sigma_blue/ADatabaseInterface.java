package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Interface for the Database interface classes.
 * @param <T> is the Object that is being stored in the database. i.e., Item, Tag
 */
public abstract class ADatabaseInterface<T> {


    /**
     * Generic method for adding a new document to a collection being pointed
     * to.
     * @param cr is the collection reference that is having a new document added
     * @param item is the object that is being added as a document to the
     *             collection.
     * @param fn is a Function that converts the item of type T into a HashMap
     *           with String and String as the key and value respectively. This
     *           HashMap is of the type that can be converted into a document.
     * @param docID Is the unique docID that belongs to the item. If the doc
     *              ID already exists in the database, then the document will
     *              just be updated.
     * @param <T> Is the item object type. e.g., Item, Tag.
     */
    protected static <T> void addDocument(final CollectionReference cr,
                                       final T item,
                                       final Function<T, HashMap<String,
                                               String>> fn,
                                       final String docID) {
        cr.document(docID).set(fn.apply(item));
    }

    /**
     * Generic method that produces a new list containing objects that are
     * created from the querysnapshot.
     * @param q QuerySnapshot object that will be parsed. Obtained directly from
     *          the listener.
     * @param fn    Function that will convert a single document from the
     *              snapshot into the desired object type.
     * @param <T>   The final object type that is being retrieved. e.g., Item
     *           or Tags.
     */
    protected static <T> List<T> loadArray(final QuerySnapshot q,
                                        final Function<QueryDocumentSnapshot,
                                                T> fn) {
        List<T> list = new ArrayList<>();
        for (QueryDocumentSnapshot qd : q) {
            list.add(fn.apply(qd));
        }
        return list;
    }
}
