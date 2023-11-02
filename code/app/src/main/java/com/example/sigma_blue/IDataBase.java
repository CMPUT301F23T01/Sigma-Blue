package com.example.sigma_blue;

import java.util.ArrayList;

/**
 * Interface for the Database interface classes.
 * @param <T> is the Object that is being stored in the database. i.e., Item, Tag
 */
public interface IDataBase<T> {
    /**
     * Saves the ArrayList of items to the database.
     * @param items is an object that is being saved.
     */
    public void saveToDB(ArrayList<T> items);
    public ArrayList<T> refreshFromDB();
}
