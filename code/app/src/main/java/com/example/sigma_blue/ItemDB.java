package com.example.sigma_blue;

import java.util.ArrayList;

public class ItemDB implements IDataBase<Item>{
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
