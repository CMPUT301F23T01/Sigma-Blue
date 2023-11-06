package com.example.sigma_blue;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ItemListTest {
    ItemList itemListUT;

    /**
     * Recreating the Class Under Test
     */
    @Before
    public void makeItemList() {
        itemListUT = ItemList.newInstance();
    }

    @After
    public void tearDown() {
        itemListUT = null;
    }

    /**
     * Testing if the List could properly keep count.
     */
    @Test
    public void sizeTest() {
        itemListUT.add(placeHolderItem1());
        assertTrue(itemListUT.size() == 1);
    }

    /* Helper functions */

    private Item placeHolderItem1() {
        return new Item(
                "ThinkPad",
                new Date(),
                "Lenovo ThinkPad",
                "It's cool",
                "Lenovo",
                "T470",
                300f
        );
    }
}
