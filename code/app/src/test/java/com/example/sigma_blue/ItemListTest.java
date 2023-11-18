package com.example.sigma_blue;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertFalse;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.item.ItemDB;
import com.example.sigma_blue.entity.item.ItemFactory;
import com.example.sigma_blue.entity.item.ItemList;
import com.example.sigma_blue.entity.item.ItemListAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class ItemListTest {
    /* Under test */
    ItemList itemListUT;
    ItemFactory itemF;
    ArrayList<Item> fakeItemDB;

    /* Mock classes */
    @Mock
    ItemDB mockDBHandler;
    @Mock
    ItemListAdapter itemListAdapter;

    /**
     * Recreating the Class Under Test
     */
    @Before
    public void makeItemList() {
        Account testAccount = new Account("Watrina 4",
                "fdsoijj191");
        itemListUT = ItemList.newInstance(testAccount, mockDBHandler,
                itemListAdapter, new ArrayList<>());
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
        itemF.setFactoryValue(10d);

        assertFalse(itemListUT.sumValues.apply(itemListUT.getList()).isPresent());

        itemListUT.add(itemF.templateItem("a"));
        itemListUT.add(itemF.templateItem("b"));
        itemListUT.add(itemF.templateItem("c"));
        itemListUT.add(itemF.templateItem("d"));
        itemListUT.add(itemF.templateItem("e"));

        if (itemListUT.sumValues.apply(itemListUT.getList()).isPresent())
            assertEquals(itemListUT.sumValues.apply(itemListUT.getList()).get(), 50f);
        else fail("Empty optional, expected optional with 50f");

    }

    /* Helper functions */

    private Item placeHolderItem1() {
        /* TODO: Move this to a test factory class. */
        return new Item(
                "ThinkPad",
                new Date(),
                "Lenovo ThinkPad",
                "It's cool",
                "Lenovo",
                "",
                "T470",
                300d
        );
    }
}
