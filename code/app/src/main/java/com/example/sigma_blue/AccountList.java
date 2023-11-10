package com.example.sigma_blue;

import android.util.Log;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Similar to ItemList and TagList. Stores a list of all user accounts
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

    /**
     * Dependency injection constructor.
     * @param aDB database handler
     * @param aLst local list
     */
    public AccountList(AccountDB aDB, List<Account> aLst) {
        accountDB = aDB;
        accList = aLst;
    }

    // placholder for now
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

    /**
     * Method for checking if a given Account object exists in the AccountList
     * @param account is the Account object we are checking for existance in AccountList
     * @return match is a boolean that returns true if the account is contained in the list, and false if not
     */
    public boolean contains(Account account) {
        if (accList == null) {
            Log.w("DEBUG", "Checking null accounts list");
            return false;
        } else {
            boolean match = false;
            for (Account acc : accList) {
                if (acc.checkPassword(account.getPassword()) && acc.checkUsername(account.getUsername())) {
                    match = true;
                }
            }
            return match;
        }
    }

    @Override
    public void setList(List<Account> lst) {
        this.accList = lst;
    }

    @Override
    public void updateUI() {
    }

    /**
     * Loads the database collection into a List of objects.
     * @param q QuerySnapshot (database collection)
     * @return a List of Tag objects that represent the tags held by the account
     */
    @Override
    public List<Account> loadArray(QuerySnapshot q) {
        return AccountDB.loadArray(q, Account.accountOfDocument);
    }


    @Override
    public void startListening() {
        accountDB.startListening(accountDB.getCollectionReference(), this);
    }
}
