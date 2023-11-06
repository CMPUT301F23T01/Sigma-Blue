package com.example.sigma_blue;

import static junit.framework.TestCase.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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
