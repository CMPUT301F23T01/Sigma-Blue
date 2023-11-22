package com.example.sigma_blue.entity.account;

import android.util.Log;

import com.example.sigma_blue.database.IDatabaseList;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Similar to ItemList and TagList. Stores a list of all user accounts. Allows
 * for persistent storage of accounts.
 */
public class AccountList implements Serializable, IDatabaseList<Account> {
    private List<Account> accList;
    private final AccountDB accountDB;

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
        accList = new ArrayList<Account>();
        accountDB = AccountDB.newInstance();
        startListening();
    }

    /**
     * Method for adding an account to the AccountList
     * @param account is the Account object to be added
     */
    public void add(Account account) {
        accList.add(account);
        this.accountDB.add(account);
    }

//    /**
//     * Method for checking if a given Account object exists in the AccountList
//     * @param account is the Account object we are checking for existance in AccountList
//     * @return match is a boolean that returns true if the account is contained in the list, and false if not
//     */
//    public boolean contains(Account account) {
//        if (accList == null) {
//            Log.w("DEBUG", "Checking null accounts list");
//            return false;
//        } else {
//            return accList.contains(account);
//        }
//    }

    /**
     * Method for checking if a username/password pair is valid.
     */
    public boolean validAccount(Account account) {
        if (!this.getAccList().contains(account)) {
            return false;
        }
        boolean valid = false;
        for (Account a : this.accList) {
            if (a.checkUsername(account.getUsername()) && a.checkPassword(account.getPassword())) {
                return true;
            }
        }
        return valid;
    }

    /**
     * Used for concurrent list update
     * // TODO: Add a semaphore.
     * @param lst
     */
    @Override
    public void setList(List<Account> lst) {
        this.accList = lst;
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
     * Installs the listener to the database that updates the list as the
     * database changes.
     */
    @Override
    public void startListening() {
        accountDB.startListening(accountDB.getCollectionReference(), this);
    }

    public List<Account> getAccList() {
        return accList;
    }
}
