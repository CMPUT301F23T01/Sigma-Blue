package com.example.sigma_blue;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class handles database handling.
 */
public class ItemDB extends ADatabaseInterface<Item> {

    private CollectionReference itemsRef;
    private Account account;

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
        this.itemsRef = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                .document(a.getUsername())
                .collection(DatabaseNames.ITEMS_COLLECTION.getName());
        this.account = a;
    }

    /**
     * This method adds a listener to a user's item collection.
     * @param adapter is the adapter that is getting updated.
     */
    public void startListening(ItemListAdapter adapter,
                               ItemList list) {
        itemsRef.addSnapshotListener(
                (q, e) -> {
                    if (q != null) {
                        list.setList(loadArray(q));
                        adapter.notifyDataSetChanged();
                        adapter.updateSumView();
                    }
                }
        );
    }

    /**
     * Method for adding a new item to the database.
     * @param item is an Item object being added to the database.
     */
    public void add(final Item item) {
        addDocument(itemsRef, item, v -> {
            HashMap<String, String> ret = new HashMap<>();
            ret.put("NAME", item.getName());
            ret.put("DATE", item.getDate().toString());
            ret.put("MAKE", item.getMake());
            ret.put("MODEL", item.getModel());
            ret.put("VALUE", String.valueOf(item.getValue()));
            return ret;
        }, item.getDocID());
        Log.v("Database Interaction", "Saved Item: "+ item.getDocID());
    }

    /**
     * This method removes the specified item from the database.
     * @param item is an item object that is being removed from the database.
     */
    public void remove(final Item item) {
        removeDocument(itemsRef, item);
    }

    /**
     * Method that will just return an Item List implementation
     * @param q is a QuerySnapshot that is being converted into a list.
     * @return a list of items.
     */
    private List<Item> loadArray(final QuerySnapshot q) {
        return loadArray(q, v -> {
            return Item.newInstance(
                    v.getString("NAME"),
                    new Date(),    // TODO: Work on unifying date
                    v.getString("MAKE"),
                    v.getString("MODEL"),
                    Float.parseFloat(v.getString("VALUE"))
            );
        });
    }

    public Account getAccount() {
        return this.account;
    }
}
