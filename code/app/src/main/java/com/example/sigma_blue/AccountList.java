package com.example.sigma_blue;

import android.util.Log;

import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Similar to ItemList and TagList. Stores a list of all user accounts
 */
public class AccountList implements Serializable, IDatabaseList<Account> {
    private List<Account> accList;
    private AccountDB accountDB;

    public static AccountList newInstance() {
        return new AccountList();
    }

    // placholder for now
    public AccountList() {
        accList = new ArrayList<Account>();
        accountDB = AccountDB.newInstance();
        startListening();
    }

    public void add(Account account) {
        accList.add(account);
        this.accountDB.add(account);
    }

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

    @Override
    public List<Account> loadArray(QuerySnapshot q) {
        return AccountDB.loadArray(q, v-> {
            return new Account(
                    v.getString("USERNAME"),
                    v.getString("PASSWORD")
            );
        });
    }


    @Override
    public void startListening() {
        accountDB.startListening(accountDB.getCollectionReference(), this);
    }
}
