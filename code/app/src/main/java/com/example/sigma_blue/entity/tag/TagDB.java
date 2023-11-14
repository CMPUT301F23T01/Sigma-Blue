package com.example.sigma_blue.entity.tag;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.DatabaseNames;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Interfaces with the database to keep the tags up to date. Should this be included in TagList?
 * Ideally this class will only be used from TagList.
 */
public class TagDB extends ADatabaseHandler<Tag> {

    private final Account account;
    private final CollectionReference tagRef;

    public static TagDB newInstance(Account a) {
        TagDB ret = new TagDB(a);
        return ret;
    }

    /**
     * Constructor. Account is mandatory.
     * @param a is the Account object that this the current tag database will be
     *          holding account for.
     */
    private TagDB(Account a) {
        account = a;
        tagRef = FirebaseFirestore.getInstance()
                .collection(DatabaseNames.PRIMARY_COLLECTION.getName())
                .document(a.getUsername())
                .collection(DatabaseNames.TAGS_COLLECTION.getName());
    }

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

    /**
     * Adds a new item to the database
     * @param item Tag being stored
     */
    @Override
    public void add(Tag item) {
        addDocument(tagRef, item, Tag.hashMapOfTag, item.getDocID());
    }

    /**
     * Removes the specified item from the database
     * @param item the Tag class
     */
    @Override
    public void remove(Tag item) {
        removeDocument(tagRef, item);
    }

    /**
     * Getter for the collection reference.
     * @return the Collection reference being held by the DB handler.
     */
    @Override
    public CollectionReference getCollectionReference() {
        return this.tagRef;
    }

    /**
     * Returns the account is being hosted.
     * @return an Account object representing the tag that is being viewed.
     */
    private Account getAccount() {
        return account;
    }
}
