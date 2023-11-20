package com.example.sigma_blue.entity.account;

import com.example.sigma_blue.database.IDatabaseItem;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;

/**
 * Stores information about a user account.
 */
public class Account implements Serializable, IDatabaseItem<Account> {
    private String username;
    private String password;

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
        this.password = aPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return Objects.equals(this.password, aPassword);
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
            accountOfDocument = q -> {
        return new  Account(
                q.getString(USERNAME),
                q.getString(PASSWORD)
        );
    };

    /**
     * Function that converts Account to HashMap of String and String.
     */
    public static final Function<IDatabaseItem<Account>, HashMap<String, Object>>
            hashMapOfEntity = a -> {
        HashMap<String, Object> ret = new HashMap<>();
        ret.put(USERNAME, ((Account) a).getUsername());
        ret.put(PASSWORD, ((Account) a).getPassword());
        return ret;
    };

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
     * return the hashmap function
     * @return
     */
    public Function<IDatabaseItem<Account>, HashMap<String, Object>> getHashMapOfEntity() {
        return this.hashMapOfEntity;
    }

    @Override
    public Account getInstance() {
        return this;
    }
}
