package com.example.sigma_blue;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Interfaces with the database to keep the tags up to date. Should this be included in TagList?
 * Ideally this class will only be used from TagList.
 */
public class TagDB {
    /**
     * Update the database with the current tags
     */
    public void saveToDB(TagList tList) {};

    /**
     * Update the
     * @return new list of tags from the database.
     */
    public ArrayList<Tag> refreshFromDB(){
        ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Generic Tag", 0xffff0000));
        return tags;
    };

    //missing a constructor that takes a bunch of setup params.
}
