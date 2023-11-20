package com.example.sigma_blue.entity.account;

import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.DatabaseNames;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Handles database interactions for each account.
 */
public class AccountDB extends ADatabaseHandler<Account> {
    /**
     * newInstance method for hiding construction.
     * @return a new AccountDB instance.
     */
    public static AccountDB newInstance() {
        return new AccountDB();
    }

    /**
     * Constructor for AccountDB. Links Firestore instance to the class itself.
     */
    public AccountDB() {
        ref = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName());
    }

    /**
     * Database dependency injection constructor
     * @param fS firestore object.
     */
    public AccountDB(FirebaseFirestore fS) {
        ref = fS.collection(DatabaseNames.PRIMARY_COLLECTION
                .getName());
    }

    /**
     * Returns the collection reference for testing.
     * @return the CollectionReference
     */
    @Override
    public CollectionReference getCollectionReference() {
        return this.ref;
    }
}
