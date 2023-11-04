package com.example.sigma_blue;

import java.util.ArrayList;

/**
 * Keeps track of a list of tags. Also allows for sync to the DB (well in the future...)
 */
public class TagList {
    private ArrayList<Tag> tags;
    final private TagDB tagDB;

    public TagList(TagDB tagDB) {
        this.tagDB = tagDB;
    }

    public void saveTagsToDB() {
        tagDB.saveToDB(this);
    }

    public void refreshTagsFromDB() {
        this.tags = tagDB.refreshFromDB();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public boolean containsTag(Tag tag) {
        return tags.contains(tag);
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }
}