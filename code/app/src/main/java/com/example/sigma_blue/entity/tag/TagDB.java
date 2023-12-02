package com.example.sigma_blue.entity.tag;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.DatabaseNames;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Interfaces with the database to keep the tags up to date.
 * Ideally this class will only be used from TagList.
 */
public class TagDB extends ADatabaseHandler<Tag> {

    private final Account account;

    public static TagDB newInstance(Account a) {
        TagDB ret = new TagDB(a);
        return ret;
    }

    /**
     * Constructor. Account is mandatory.
     * @param a is the Account object that this the current tag database will be
     *          holding account for.
     */
    public TagDB(Account a) {
        account = a;
        ref = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                .document(a.getUsername())
                .collection(DatabaseNames.TAGS_COLLECTION.getName());
    }

    /**
     * Returns the document reference used for referencing by other documents
     * @param item is the Tag object that is providing the document reference
     * @return
     */
    public DocumentReference getDocRef(Tag item) {
        // Building with the assumption of the tag existing (may need change)
        return ref.document(item.getDocID());
    }

    /**
     * Getter for the collection reference.
     * @return the Collection reference being held by the DB handler.
     */
    @Override
    public CollectionReference getCollectionReference() {
        return this.ref;
    }
}
