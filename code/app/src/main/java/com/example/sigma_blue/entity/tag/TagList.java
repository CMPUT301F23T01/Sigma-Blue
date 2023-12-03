package com.example.sigma_blue.entity.tag;

import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.entity.AEntityList;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.IDatabaseList;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.function.Function;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Keeps track of a global use of tags. Also allows for sync to the DB
 */
public class TagList extends AEntityList<Tag> {
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
     * Factory creation method. Uses the current account in global context
     * @return an instance of the TagList object containing a connection
     * to the database that stores all of the Tags that the user has defined.
     */
    public static TagList newInstance() {
        return new TagList();
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

    private TagList(TagDB tagDB) {
        super();
        this.dbHandler = tagDB;
    }

    private TagList() {
        super();
        this.globalContext = GlobalContext.getInstance();
        this.dbHandler = TagDB.newInstance(globalContext.getAccount());
    }

    /**
     * Updates the UI elements that are dependent on this class.
     */
    @Override
    public void updateUI() {
        adapter.setList(this.entityList);
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
}
