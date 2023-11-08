package com.example.sigma_blue;

import android.util.Log;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Similar to ItemList and TagList. Stores a list of all user accounts
 */
public class AccountList implements Serializable {
    private ArrayList<Account> accList;
    // placholder for now
    public AccountList() {
        accList = new ArrayList<Account>();
        Account a = new Account("user1", "password");
        Account b = new Account("user2", "password");
        Account c = new Account("user3", "password");

        accList.add(a);
        accList.add(b);
        accList.add(c);
    }
    public void add(Account account) {
        accList.add(account);
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
}
