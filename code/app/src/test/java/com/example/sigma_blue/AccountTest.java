package com.example.sigma_blue;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class AccountTest {

    @Test
    public void testAccountCheckUsername(){
        // build a mock account object
        String aUsername = "goodName";
        String aPassword = "goodPassword";
        Account testAccount = new Account(aUsername, aPassword);

        // test checkUsername against a different username
        String badUsername = "badName";
        assertFalse(testAccount.checkUsername(badUsername));

        // test checkUsername against a matching username
        String goodUsername = "goodName";
        assertTrue(testAccount.checkUsername(goodUsername));
    }

    @Test
    public void testAccountCheckPassword(){
        // build a mock account object
        String aUsername = "goodName";
        String aPassword = "goodPassword";
        Account testAccount = new Account(aUsername, aPassword);

        // test checkPassword against a different password
        String badPassword = "badPassword";
        assertFalse(testAccount.checkPassword(badPassword));

        // test checkPassword against a matching password
        String goodPassword = "goodPassword";
        assertTrue(testAccount.checkPassword(goodPassword));
    }
}
