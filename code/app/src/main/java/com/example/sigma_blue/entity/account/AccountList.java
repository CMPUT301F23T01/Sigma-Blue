package com.example.sigma_blue.entity.account;

import android.util.Log;

import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.entity.AEntityList;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Similar to ItemList and TagList. Stores a list of all user accounts. Allows
 * for persistent storage of accounts.
 */
public class AccountList extends AEntityList<Account> implements Serializable, IDatabaseList<Account> {
    /**
     * newInstance method to hide construction
     * @return a new AccountList instance
     */
    public static AccountList newInstance() {
        return new AccountList();
    }

//    /**
//     * Dependency injection constructor.
//     * @param aDB database handler
//     * @param aLst local list
//     */
//    public AccountList(AccountDB aDB, List<Account> aLst) {
//        accountDB = aDB;
//        accList = aLst;
//    }

    /**
     * Constructor for an AccountList
     */
    public AccountList() {
        this.entityList = new ArrayList<Account>();
        this.dbHandler = AccountDB.newInstance();
        startListening();
    }

    /**
     * Method for checking if a username/password pair is valid.
     */
    public boolean validAccount(Account account) {
        if (!entityList.contains(account)) {
            return false;
        }
        boolean valid = false;
        for (Account a : this.entityList) {
            if (a.checkUsername(account.getUsername()) && a.checkPassword(account.getPassword())) {
                return true;
            }
        }
        return valid;
    }

    /**
     * Does nothing as no UI for the account list
     */
    @Override
    public void updateUI() {}

    /**
     * Loads the database collection into a List of objects.
     * @param q QuerySnapshot (database collection)
     * @return a List of Tag objects that represent the tags held by the account
     */
    @Override
    public List<Account> loadArray(QuerySnapshot q) {
        return AccountDB.loadArray(q, Account.accountOfDocument);
    }

    /**
     * Updates the list held in this class. no adapter for accounts, so override is needed
     * @param list is the list that is replacing the current list.
     */
    public void setList(final ArrayList<Account> list) {
        this.entityList = list;
    }
}
