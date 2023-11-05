package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ItemListTest {
    ItemList itemListUT;
    ItemFactory itemF;

    /**
     * Recreating the Class Under Test
     */
    @Before
    public void makeItemList() {
        itemListUT = ItemList.newInstance();
    }

    @Before
    public void makeItemFactory() {
        itemF = ItemFactory.newInstance(new Date());
    }

    @After
    public void tearDown() {
        itemListUT = null;
        itemF = null;
    }

    /**
     * Testing if the List could properly keep count.
     */
    @Test
    public void sizeTest() {
        itemListUT.add(placeHolderItem1());
        Assert.assertEquals(1, itemListUT.size());
    }

    /**
     * Testing the sumValue method.
     * @author Bach
     */
    @Test
    public void sumValuesTest() {
        itemF.setFactoryValue(10f);

        assertFalse(itemListUT.sumValues().isPresent());

        itemListUT.add(itemF.templateItem("a"));
        itemListUT.add(itemF.templateItem("b"));
        itemListUT.add(itemF.templateItem("c"));
        itemListUT.add(itemF.templateItem("d"));
        itemListUT.add(itemF.templateItem("e"));

        if (itemListUT.sumValues().isPresent())
            assertEquals(itemListUT.sumValues().get(), 50f);
        else fail("Empty optional, expected optional with 50f");

    }

    /* Helper functions */

    private Item placeHolderItem1() {
        /* TODO: Move this to a test factory class. */
        return new Item(
                "ThinkPad",
                new Date(),
                "Lenovo ThinkPad",
                "Lenovo",
                "T470",
                300f
        );
    }
}
