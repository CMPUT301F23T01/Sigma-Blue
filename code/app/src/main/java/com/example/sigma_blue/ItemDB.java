package com.example.sigma_blue;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class ItemDB{

    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private Account account;

    private boolean loginFlag = false;

    private final String pwdPath = "AccountInfo";

    private List<Item> dataList;

    /**
     * newInstance method for hiding construction.
     * @param a is the account that is doing the database transactions.
     * @return a new ItemDB instance tied to the account.
     */
    public ItemDB newInstance(Account a) {
        ItemDB ret = new ItemDB();
        ret.setAccount(a);
        return ret;
    }

    /** consructor of the ItemDB
     *
     */
    public ItemDB() {
        this.db = FirebaseFirestore.getInstance();
    }

    private void setAccount(Account a) {
        this.itemsRef = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                .document(a.getUsername())
                .collection(DatabaseNames.ITEMS.getName());
        this.account = a;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void startListening() {
        itemsRef.addSnapshotListener(
                (q, e) -> {
                    if (q != null) {
                        dataList.clear();
                        this.dataList = loadItemArray(q);
                    }
                }
        );
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
    public static <T> List<T> loadArray(final QuerySnapshot q,
                                     final Function<QueryDocumentSnapshot,
                                             T> fn) {
        List<T> list = new ArrayList<>();
        for (QueryDocumentSnapshot qd : q) {
            list.add(fn.apply(qd));
        }
        return list;
    }

    /**
     * Method that will just return an Item List implementation
     * @param q
     */
    private List<Item> loadItemArray(final QuerySnapshot q) {
        return loadArray(q, v -> {
            return Item.newInstance(
                    v.getId(),
                    v.getDate("DATE"),
                    v.getString("MAKE"),
                    v.getString("MODEL"),
                    v.getDouble("VALUE").floatValue()
            );
        });
    }

    //TRY this to see this method is not working
    /*
    public void login (String userName, String password) {

        itemsRef.document(userName)
                .collection(pwdPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loginUser = userName;
                        //issue: loginUser is not set
                    }
                });
        //wait until the string set
        while (loginUser.isEmpty());
        /* OR this
        for (int i = 0; i < 10000000; i ++);

        //may try await(), but not allowed in main process
     */




   /* public void login (String userName, String password) {
        loginCheck(userName, new OnCompleteCallback() {
            @Override
            public void onComplete(boolean success, String userName, ItemDB idb) {
                idb.setLoginUser(userName);
            }
        }, this);
    }

    public interface OnCompleteCallback{
        void onComplete(boolean success, String uName, ItemDB idb);
    }
    public void loginCheck(final String userName,final OnCompleteCallback callback, final ItemDB idb) {
        itemsRef.document(userName)
                .collection(pwdPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        callback.onComplete(true, userName, idb);
                    }
                });
    }*/

/*    @Override
    public ArrayList<Item> refreshFromDB() {
        itemsRef.document(loginUser)
                .collection("Items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        dataList.clear();
                    }
                });
        return dataList;
    }*/

    public Account getAccount() {
        return this.account;
    }

    public interface ItemDBInteraction {
        void login (ItemDB idb, String userName, String password, Context Activity);
        ArrayList<Item> refreshFromDB(ItemDB idb);
    }
}
