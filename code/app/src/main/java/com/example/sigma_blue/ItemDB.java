package com.example.sigma_blue;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ItemDB implements IDataBase<Item>{

    private FirebaseFirestore db;

    /** consructor of the ItemDB
     *
     */
    public ItemDB() {
        this.db = FirebaseFirestore.getInstance();
    }

    /**
     * Saves the ArrayList of items to the database.
     *
     * @param items is an object that is being saved.
     */
    @Override
    public void saveToDB(ArrayList<Item> items) {

    }

    @Override
    public ArrayList<Item> refreshFromDB() {
        return null;
    }
}
