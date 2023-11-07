package com.example.sigma_blue;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemDB{

    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private String loginUser;

    private boolean loginFlag = false;

    private final String pwdPath = "AccountInfo";

    private ArrayList<Item> dataList;

    /**
     * newInstance method for hiding construction.
     * @param a is the account that is doing the database transactions.
     * @return
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
    }

    public FirebaseFirestore getDb() {
        return db;
    }


    public void signUp (String userName, String password) {

        itemsRef.document(userName)
                .collection(pwdPath)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // if the user name do not exist, create account
                            if (task.getResult().size() == 0){
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Password", password);
                                itemsRef.document(userName).collection("AccountInfo").document("Password").set(data);
                            }else{
                                Log.d("signup error", "User already exist");
                            }

                        } else {
                            Log.d("Document Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    public void setLoginUser (String userName) {
        this.loginUser = userName;
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

    /**
     * Saves the ArrayList of items to the database.
     *
     * @param items is an object that is being saved.
     */
    public void saveToDB(ArrayList<Item> items) {
        if (loginUser == null) {
            Log.d("login error", "User not login!");
        } else {
            for (Item i : items) {
                itemsRef.document(loginUser).collection("Items").document(i.getName()).set(i);
            }
        }
    }

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

    public interface ItemDBInteraction {
        void login (ItemDB idb, String userName, String password, Context Activity);
        ArrayList<Item> refreshFromDB(ItemDB idb);
    }
}
