package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * Handles database interactions for each account.
 */
public class AccountDB extends ADatabaseHandler<Account> {

    private CollectionReference accountPointer;

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
        accountPointer = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName());
    }

    /**
     * Database dependency injection constructor
     * @param fS firestore object.
     */
    public AccountDB(FirebaseFirestore fS) {
        accountPointer = fS.collection(DatabaseNames.PRIMARY_COLLECTION
                .getName());
    }

    /**
     * Adds a new account to the database.
     * @param item is an Account object that is being added
     */
    @Override
    public void add(Account item) {
        addDocument(accountPointer, item, Account.hashMapOfAccount,
                item.getDocID());
    }

    /**
     * Removes an existing account from the database.
     * @param item is the Account object to be removed
     */
    @Override
    public void remove(final Account item) {
        removeDocument(accountPointer, item);
    }

    @Override
    public CollectionReference getCollectionReference() {
        return this.accountPointer;
    }

}
