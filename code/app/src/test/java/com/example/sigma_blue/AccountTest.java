package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.utility.StringHasher;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;


@RunWith(MockitoJUnitRunner.class)
public class AccountTest {
    @Mock
    QueryDocumentSnapshot mockDocument;

    private List<Account> cUTs;

    @Before
    public void makeCUTs() {
        cUTs = new ArrayList<>();

        cUTs.add(new Account("testing", "password1"));
        cUTs.add(new Account("testing2", "Password2"));
        cUTs.add(new Account("Testing", "p"));
    }

    @After
    public void tearDown() {
        cUTs = null;
    }

    private HashMap<String, Object> hashMapFormatter(String username,
                                                     String password) {
        String hashedPassword = StringHasher.getHash(password);
        HashMap<String, Object> ret = new HashMap<>();
        ret.put("USERNAME", username);
        ret.put("PASSWORD", hashedPassword);
        return ret;
    }

    @Test
    public void testHashMapGeneration() {
        // Testing creation of hashmap objects from the tags
        List<HashMap<String, Object>> testVector = new ArrayList<>();
        // Not the cleanest way yet, but better than before
        testVector.add(hashMapFormatter("testing",
                "password1"));
        testVector.add( hashMapFormatter("testing2",
                "Password2"));
        testVector.add( hashMapFormatter("Testing",
                "p"));

        for (int i = 0; i < testVector.size(); i++)
            assertEquals(testVector.get(i), cUTs.get(i).getHashMapOfEntity()
                    .apply(cUTs.get(i)));
    }

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
