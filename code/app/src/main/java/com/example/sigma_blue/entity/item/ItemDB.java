package com.example.sigma_blue.entity.item;

import android.util.Log;


import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.DatabaseNames;
import com.example.sigma_blue.query.SortField;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles database handling.
 */
public class ItemDB extends ADatabaseHandler<Item> {

    private FirebaseFirestore firestoreInjection;
    private CollectionReference itemsRef;
    private Account account;
    private List<Item> items;

    /**
     * newInstance method for hiding construction.
     * @param a is the account that is doing the database transactions.
     * @return a new ItemDB instance tied to the account.
     */
    public static ItemDB newInstance(Account a) {
        ItemDB ret = new ItemDB();
        ret.setAccount(a);
        return ret;
    }

    /**
     * Firestore injection construction. Required for testing database
     * @param f the Firestore that is being used
     * @param a Account that the item database handler is controlling.
     * @return a new ItemDB instance.
     */
    public static ItemDB newInstance(FirebaseFirestore f,
                                     Account a) {
        ItemDB ret = new ItemDB();
        ret.setFirestore(f);
        ret.setAccount(a);
        return ret;
    }

    /**
     * Bare Constructor
     */
    private ItemDB() {
    }


    /**
     * Embed the account into the database. Only used when creating a new
     * instance.
     * @param a is an Account object that the instance of the database is
     *          querying.
     */
    private void setAccount(Account a) {
        /* Allows for legacy usage */
        if (this.firestoreInjection == null) {
            this.itemsRef = FirebaseFirestore.getInstance()
                    .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                    .document(a.getUsername())
                    .collection(DatabaseNames.ITEMS_COLLECTION.getName());
            firestoreInjection = FirebaseFirestore.getInstance();
        }
        /* Injection already exists */
        else this.itemsRef = firestoreInjection
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                .document(a.getUsername())
                .collection(DatabaseNames.ITEMS_COLLECTION.getName());
        this.account = a;
    }

    private void setFirestore(FirebaseFirestore f) {
        this.firestoreInjection = f;
    }

    /**
     * Method for adding a new item to the database.
     * @param item is an Item object being added to the database.
     */
    public void add(final Item item) {
        addDocument(itemsRef, item, Item.hashMapOfItem, item.getDocID());
        Log.v("Database Interaction", "Saved Item: "+ item.getDocID());
    }

    /**
     * This method removes the specified item from the database.
     * @param item is an item object that is being removed from the database.
     */
    public void remove(final Item item) {
        removeDocument(itemsRef, item);
    }

    public Account getAccount() {
        return this.account;
    }

    public CollectionReference getCollectionReference() {
        return this.itemsRef;
    }
}
