package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * This class handles databse handling for accounts
 */
public class AccountDB extends ADatabaseHandler<Account> {

    private CollectionReference accountPointer;
    private List<Account> accounts;
    public Function<Account, HashMap<String, String>> hashMapConverter = v ->
    {
        HashMap<String, String> ret = new HashMap<>();
        ret.put("USERNAME", v.getUsername());
        ret.put("PASSWORD", v.getPassword());
        return ret;
    };

    /**
     * newInstance method for hiding construction.
     * @return a new AccountDB instance.
     */
    public static AccountDB newInstance() {
        return new AccountDB();
    }

    /**
     * Constructor for AccountDB
     */
    private AccountDB() {
        accountPointer = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName());
    }

    /**
     * Method for adding a new account to the database
     * @param item is an Account object that is being added
     */
    @Override
    public void add(Account item) {
        addDocument(accountPointer, item, hashMapConverter, item.getDocID());
    }

    /**
     * Method for removing an existing account from the database
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
