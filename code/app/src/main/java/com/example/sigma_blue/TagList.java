package com.example.sigma_blue;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Keeps track of a global use of tags. Also allows for sync to the DB (well in the future...)
 */
public class TagList implements Serializable {
    private ArrayList<Tag> tags;
    final private TagDB tagDB;

    public TagList(TagDB tagDB) {
        this.tagDB = tagDB;
        this.tags = new ArrayList<>();
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

    public void removeTag(int position) {
        tags.remove(position);
    }

    public boolean containsTag(Tag tag) {
        return tags.contains(tag);
    }

    public ArrayList<Tag> getTags() {
        return this.tags;
    }

    public int getCount() { return tags.size(); }

    public Tag getItem(int position) {
        return tags.get(position);
    }
}
