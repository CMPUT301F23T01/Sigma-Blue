package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Interfaces with the database to keep the tags up to date. Should this be included in TagList?
 * Ideally this class will only be used from TagList.
 */
public class TagDB extends ADatabaseHandler<Tag> {

    private final Account account;
    private final CollectionReference accountRef;

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
        accountRef = FirebaseFirestore.getInstance()
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

    @Override
    public void add(Tag item) {

    }

    @Override
    public void remove(Tag item) {
    }

    /**
     * Getter for the collection reference.
     * @return the Collection reference being held by the DB handler.
     */
    @Override
    public CollectionReference getCollectionReference() {
        return this.accountRef;
    }

    /**
     * Returns the account is being hosted.
     * @return an Account object representing the tag that is being viewed.
     */
    private Account getAccount() {
        return account;
    }
}
