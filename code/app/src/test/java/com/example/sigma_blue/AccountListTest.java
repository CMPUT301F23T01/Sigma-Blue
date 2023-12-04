package com.example.sigma_blue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountDB;
import com.example.sigma_blue.entity.account.AccountList;

import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Empty test class, as there is no method in this class to test right now.
 */
@RunWith(MockitoJUnitRunner.class)
public class AccountListTest {
    private AccountList classUnderTest;
    private List<Account> dummyAccounts;

    @Mock
    AccountDB dummyHandler;

    @Before
    public void initializeCUT() {
        dummyAccounts = makePlaceholderAccounts();
        this.classUnderTest = new AccountList(dummyHandler, dummyAccounts);
    }

    @After
    public void tearDown() {
        classUnderTest = null;
        dummyAccounts = null;
        dummyHandler = null;
    }

    @Test
    public void testSetList() {
        ArrayList<Account> newAccounts = makePlaceholderAccounts2();
        classUnderTest.setList(newAccounts);

        assertEquals(4, classUnderTest.getList().size());

        boolean replaced = false;

        for (Account a: makePlaceholderAccounts()) {
            if (newAccounts.contains(a)) {
                replaced = false;
                break;
            }
            replaced = true;
        }
        assertTrue(replaced);
    }

    @Test
    public void testValidAccounts() {
        ArrayList<Account> invalidAccounts = makeInvalidAccounts();

        // these accounts should all be invalid.
        for (Account iA: invalidAccounts) {
            assertEquals(false, classUnderTest.validAccount(iA));
        }
    }

    // Helper functions
    private ArrayList<Account> makePlaceholderAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("user1", "pass1"));
        accounts.add(new Account("user2", "pass2"));
        accounts.add(new Account("user3", "pass3"));

        return accounts;
    }

    private ArrayList<Account> makePlaceholderAccounts2() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("user1part2", "pass1part2"));
        accounts.add(new Account("user2part2", "pass2part2"));
        accounts.add(new Account("user3part2", "pass3part2"));
        accounts.add(new Account("user4part2", "pass4part2"));

        return accounts;
    }

    private ArrayList<Account> makeInvalidAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account("user1", "invalidpass1"));
        accounts.add(new Account("user2", "invalidpass2"));
        accounts.add(new Account("user3", "invalidpass3"));

        return accounts;
    }

}
