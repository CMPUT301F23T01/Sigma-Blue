package com.example.sigma_blue;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This class creates the base database structure that an account will need when
 * using this application
 */
public class DatabaseInitializer {
    private CollectionReference dbRef;
    private Boolean exists;

    /**
     * Injection construction for better re-usability and defaults.
     * @return the DatabaseInitializer object that will be used to create a new
     * file structure.
     */
    public static DatabaseInitializer newInstance() {
        return new DatabaseInitializer( FirebaseFirestore.getInstance());
    }

    /**
     * Private constructor to enforce newInstance usage.
     * @param d is the database object
     */
    private DatabaseInitializer(final FirebaseFirestore d) {
        this.dbRef = d.collection(DatabaseNames.PRIMARY_COLLECTION.getName());
        this.exists = false;
    }

    /**
     * Method for checking if the account has been inserted into the database.
     * @param a is the Account that is being checked.
     */
    public boolean checkExistence(final Account a) {
       dbRef.document(a.getUsername())
               .collection(DatabaseNames.ACCOUNT_INFO_COLLECTION.getName())
               .document(DatabaseNames.USER_INFO_DOCUMENT.getName());
       return this.exists;
    }

    /**
     * Method for generating file structure for an account
     * @param a is the Account object connected to the generating file structure
     */
    public void generateFileStructure(final Account a) {
        HashMap<String, String> entry = documentOfAccount(a);
        dbRef.document(a.getUsername())
                .collection(DatabaseNames.ACCOUNT_INFO_COLLECTION.getName())
                .document(DatabaseNames.USER_INFO_DOCUMENT.getName())
                .set(entry);
    }

    /**
     * Method for creating document for an account
     * @param a is the Account object whose document is being made
     * @return doc is the document that was made, of type HashMap<String, String>
     */
    public HashMap<String, String> documentOfAccount(final Account a) {
        HashMap<String, String> doc = new HashMap<>();
        doc.put("USERNAME", a.getUsername());
        doc.put("PASSWORD", a.getPassword());
        return doc;
    }
}
