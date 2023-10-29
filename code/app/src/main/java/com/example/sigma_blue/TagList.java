package com.example.sigma_blue;

import java.util.ArrayList;

public class TagList {
    private ArrayList<Tag> tags;

    public void saveTagsToDB(TagDB DB) {
        DB.saveToDB();
    }

    public void refreshTagsFromDB(TagDB DB) {
        DB.refreshFromDB();
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public boolean containsTag(Tag tag) {
        return tags.contains(tag);
    }
}
