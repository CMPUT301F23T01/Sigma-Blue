package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.sigma_blue.entity.account.Account;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class AccountTest {
    @Mock
    QueryDocumentSnapshot mockDocument;

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

    /**
     * Tests the conversion between the document query and the account class.
     */
    @Test
    public void testAccountOfDocument() {
        Account expected = new Account("Watrina",
                "magicMushrooms");
        Mockito.when(mockDocument.getString(Account.USERNAME))
                .thenReturn("Watrina");
        Mockito.when(mockDocument.getString(Account.PASSWORD))
                .thenReturn("magicMushroom");

        assertEquals(expected, Account.accountOfDocument.apply(mockDocument));
        assertThrows(NullPointerException.class,
                () -> Account.accountOfDocument.apply(null));
    }

}
