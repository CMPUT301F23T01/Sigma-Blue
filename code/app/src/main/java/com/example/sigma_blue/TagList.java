package com.example.sigma_blue;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Keeps track of a list of tags. Also allows for sync to the DB (well in the future...)
 */
public class TagList implements IDatabaseList<Tag> {
    private List<Tag> tags;
    final private TagDB tagDB;

    public static final String LABEL = "LABEL", COLOR = "COLOR";

    public final Function<Tag, HashMap<String, String>> hashMapOfTag = t -> {
        HashMap<String, String> ret = new HashMap<>();
        ret.put(LABEL, t.getTagText());
        ret.put(COLOR, t.getColourString());
        return ret;
    };

    public TagList newInstance(TagDB db) {
        TagList ret = new TagList(db);
        return ret;
    }
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
        if (!tags.contains(tag)) {
            tags.add(tag);
            tagDB.add(tag);
        }
    }

    public void remove(Tag tag) {
        if (tags.contains(tag)) {
            tagDB.remove(tag);
            tags.remove(tag);
        }
    }

    public boolean containsTag(Tag tag) {
        return tags.contains(tag);
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    /**
     * Sets a new list.
     * @param lst is a List object that is replacing it.
     */
    @Override
    public void setList(List<Tag> lst) {
        this.tags = lst;
    }

    /**
     * Updates the UI elements that are dependent on this class.
     */
    @Override
    public void updateUI() {

    }

    /**
     * Converts all documents in the query snapshot into a List.
     * @param q is the query snapshot that is being converted into a list.
     * @return the loaded List object containing the Tags
     */
    @Override
    public List<Tag> loadArray(QuerySnapshot q) {
        return TagDB.loadArray(q, v -> {
            return new Tag(v.getString("LABEL"),
                    Integer.valueOf(v.getString("COLOR")));
        });
    }

    /**
     * Sets up a listening thread for changes to the database collection. This
     * method will update the state of the tag list.
     */
    @Override
    public void startListening() {
        tagDB.startListening(this.tagDB.getCollectionReference(), this);
    }
}
