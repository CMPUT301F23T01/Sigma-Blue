package com.example.sigma_blue.entity.tag;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.IDatabaseList;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private TagListAdapter adapter;

    /**
     * Factory creation method that binds the given Account to
     * the TagList itself.
     * @param a The account that the user is signed in with.
     * @return an instance of the TagList object containing a connection
     * to the database that stores all of the Tags that the user has defined.
     */
    public static TagList newInstance(Account a) {
        TagDB db = TagDB.newInstance(a);
        TagList ret = new TagList(db);
        return ret;
    }

    /**
     * Factory creation method that binds the given TagDB to
     * the TagList itself.
     * @param db The TagDB object that defines the user account's
     *           connection to the database..
     * @return an instance of the TagList object containing a connection
     * to the database that stores all of the Tags that the user has defined.
     */
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
        adapter.setList(this.tags);
        adapter.notifyDataSetChanged();
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
            return new Tag(q.getString("LABEL"),q.getString("COLOR"));
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

    public void setAdapter(final TagListAdapter tagListAdapter,
                           final List<? extends Tag> selectedList) {
        this.adapter = tagListAdapter;
        this.adapter.setSelectedTags(selectedList);
    }

    public TagListAdapter getAdapter() {
        return adapter;
    }
    /**
     * Replace the old tag with a new one
     * @param updatedTag replacement tag
     * @param oldTag tag to replace
     */
    public void updateTag(Tag updatedTag, Tag oldTag) {
        this.tags.remove(oldTag);
        this.tags.add(updatedTag);
    }
}
