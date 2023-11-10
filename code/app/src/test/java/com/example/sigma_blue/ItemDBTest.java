package com.example.sigma_blue;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ItemDBTest {
    @Mock
    FirebaseFirestore mockFirestore;
    @Mock
    CollectionReference itemRef;

    /* The real classes */
    ItemDB classUnderTest;
    Date constantDate;
    List<Item> sampleItems;
    final Account testAccount = new Account("Watrina 5",
            "flksjio01");

    @Before
    public void initItemRef() {
        Mockito.when(mockFirestore.collection(DatabaseNames.PRIMARY_COLLECTION
                .getName()).document(testAccount.getUsername())
                .collection(DatabaseNames.ITEMS_COLLECTION.getName()))
                .thenReturn(itemRef);
    }
    @Before
    public void initItems() {
        constantDate = new Date();
        sampleItems = new ArrayList<Item>();
        sampleItems.add(Item.newInstance("Something in the way",
                constantDate, "Nirvana", "Nevermind", 10f));
        sampleItems.add(Item.newInstance("Something in the way",
                constantDate, "Nirvana", "Nevermind", 15f));
        sampleItems.add(Item.newInstance("Lithium",
                constantDate, "Nirvana", "Nevermind", 20f));
    }

    private void setDocumentReturn() {
    }
    @After
    public void tearDown() {
        classUnderTest = null;
    }

    @Test
    public void testGetCollectionReference() {
        classUnderTest = ItemDB.newInstance(mockFirestore, testAccount);
        Mockito.verify(mockFirestore).collection(DatabaseNames.PRIMARY_COLLECTION
                .getName()).document(testAccount.getUsername())
                .collection(DatabaseNames.ITEMS_COLLECTION.getName());
        classUnderTest.getCollectionReference();
    }
}
