package com.example.sigma_blue;

import java.io.Serializable;
import java.util.Objects;

/**
 * Stores information about a user account.
 */
public class Account implements Serializable {
    private String username;
    private String password;


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

}
