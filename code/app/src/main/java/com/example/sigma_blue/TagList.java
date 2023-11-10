package com.example.sigma_blue;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Keeps track of a global use of tags. Also allows for sync to the DB (well in the future...)
 */
public class TagList implements IDatabaseList<Tag>, Serializable {
    private ArrayList<Tag> tags;
    final private TagDB tagDB;



    public static TagList newInstance(TagDB db) {
        TagList ret = new TagList(db);
        return ret;
    }

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
        if (!tags.contains(tag)) {
            tags.add(tag);
            tagDB.add(tag);
        }
    }

    public void removeTag(Tag tag) {
        if (tags.contains(tag)) {
            tagDB.remove(tag);
            tags.remove(tag);
        }
    }

    public void removeTag(int position) {
        tags.remove(position);
    }

    public boolean containsTag(Tag tag) {
        return tags.contains(tag);
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public int getCount() { return tags.size(); }

    public Tag getItem(int position) {
        return tags.get(position);
    }

    /**
     * Sets a new list.
     * @param lst is an list object that is replacing it.
     */
    @Override
    public void setList(List<Tag> lst) {
        this.tags = (ArrayList<Tag>) lst; // Duck tape fix. If this is implemented in main I'll kms
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
        return TagDB.loadArray(q, tagOfDocument);
    }

    /**
     * Conversion from QueryDocumentSnapshot to Tag objects.
     */
    public static final Function<QueryDocumentSnapshot, Tag> tagOfDocument
            = q -> {
        if (q.getString("COLOR") != null)
            return new Tag(q.getString("LABEL"),
                    Integer.decode(q.getString("COLOR")));
        else return null;
    };

    /**
     * Sets up a listening thread for changes to the database collection. This
     * method will update the state of the tag list.
     */
    @Override
    public void startListening() {
        tagDB.startListening(this.tagDB.getCollectionReference(), this);
    }
}
