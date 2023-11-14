package com.example.sigma_blue;

import static junit.framework.TestCase.assertFalse;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.DatabaseInitializer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This testing class will not work right now. Do not depend on this one.
 */
public class DatabaseInitializerTest {
    DatabaseInitializer cUT;
    Account dNE;    // This account is supposed to not exist in the database.

    @Before
    public void makeDatabaseInitializer() {
        cUT = DatabaseInitializer.newInstance();
        dNE = new Account("Watrina", "NotRealAccount69696");
    }

    @After
    public void tearDown() {
        cUT = null;
    }

    @Test
    public void checkExistenceTest() {
        assertFalse(cUT.checkExistence(dNE));
    }
}
