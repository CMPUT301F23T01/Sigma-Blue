package com.example.sigma_blue.entity.account;

import com.example.sigma_blue.database.IDatabaseItem;
import com.example.sigma_blue.utility.StringHasher;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

/**
 * Stores information about a user account.
 */
public class Account implements Serializable, IDatabaseItem<Account> {
    private final String username;
    private final String password;

    /* Document keys */
    public static final String USERNAME = "USERNAME", PASSWORD = "PASSWORD";

    /**
     * This is constructor of account object
     * @param aUsername
     * This is the username of the account
     * @param aPassword
     * This is the password of the account
     */
    public Account(String aUsername, String aPassword) {
        this.username = aUsername;
        this.password = StringHasher.getHash(aPassword);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * This checks if a given username is same as account object's username
     * @param aUsername
     * This is the given username to be checked against the account's username
     * @return boolean
     * This is the result of the check
     * True if they are the same, false if they are not
     */
    public boolean checkUsername(String aUsername) {
        return Objects.equals(this.username, aUsername);
    }

    /**
     * This checks if a given password is same as account object's password
     * @param aPassword
     * This is the given password to be checked against the account's password
     * @return boolean
     * This is the result of the check
     * True if they are the same, false if they are not
     */
    public boolean checkPassword(String aPassword) {
        return Objects.equals(this.password, StringHasher.getHash(aPassword));
    }

    /**
     * Uniqueness based on username
     * @return the string that represents the unique id
     */
    @Override
    public String getDocID() {
        return username;
    }

    /**
     * Converts a document into an Account object.
     */
    public static final Function<QueryDocumentSnapshot, Account>
            accountOfDocument = q -> new  Account(
                    q.getString(USERNAME),
                    q.getString(PASSWORD)
            );

    /**
     * Matching account uniqueness with username.
     * @param o is the object being compared with
     * @return true if the other and this one have the same username.
     */
    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Account)) return false;
        else return ((Account) o).getUsername().equals(getUsername());
    }

    /**
     * return the hashmap conversion function
     * @return hashmap conversion Function that converts Account objects to
     * HashMaps that will be inserted into database.
     */
    public Function<IDatabaseItem<Account>, HashMap<String,
            Object>> getHashMapOfEntity() {
        return a -> {
            HashMap<String, Object> ret = new HashMap<>();
            Account account = a.getInstance();
            ret.put(USERNAME, account.getUsername());
            ret.put(PASSWORD, account.getPassword());
            return ret;
        };
    }

    /**
     * Returns the current item as its real type. Used with the IDatabaseItem
     * interface to allow access to type specific methods
     * @return the current object as its concrete type.
     */
    @Override
    public Account getInstance() {
        return this;
    }
}
