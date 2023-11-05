package com.example.sigma_blue;

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

public class ItemDB implements IDataBase<Item>{

    private FirebaseFirestore db;
    private CollectionReference itemsRef;
    private String loginUser = null;

    /** consructor of the ItemDB
     *
     */
    public ItemDB() {
        this.db = FirebaseFirestore.getInstance();
        this.itemsRef = db.collection("items");
    }

    public void signUp (String userName, String password) {
        /*
        itemsRef
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    boolean userExist = false;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains(userName)) {
                                userExist = true;
                                Log.d("signup error", "User already exist");
                            }
                        }
                        if(userExist == false) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("Password", password);
                            itemsRef.document()
                            itemsRef.document(userName).;
                        }

                    } else {
                        Log.d("Document Error", "Error getting documents: ", task.getException());
                    }
                }
            });

            */
        /*
        db.collection(userName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean userExist = false;
                        if (task.isSuccessful()) {
                            // if the user name do not exist, create account
                            if (task.getResult().size() == 0){
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Password", password);
                                db.collection(userName).document("AccountInfo").set(data);
                            }else{
                                Log.d("signup error", "User already exist");
                            }

                        } else {
                            Log.d("Document Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
            */

        itemsRef.document(userName)
                .collection("AccountInfo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // if the user name do not exist, create account
                            if (task.getResult().size() == 0){
                                HashMap<String, String> data = new HashMap<>();
                                data.put("Password", password);
                                itemsRef.document(userName).collection("AccountInfo").document("password").set(data);
                            }else{
                                Log.d("signup error", "User already exist");
                            }

                        } else {
                            Log.d("Document Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public boolean login (String userName, String password) {
        loginUser = userName;
        return true;
    }

    /**
     * Saves the ArrayList of items to the database.
     *
     * @param items is an object that is being saved.
     */
    @Override
    public void saveToDB(ArrayList<Item> items) {
        if (loginUser == null) {
            Log.d("login error", "User not login!");
        } else {
            for (Item i : items) {
                itemsRef.document(loginUser).set(i);
            }
        }
    }

    @Override
    public ArrayList<Item> refreshFromDB() {
        return null;
    }
}
