package com.example.sigma_blue;

import java.util.ArrayList;

public class ItemList {
    /* Attributes */
    private ArrayList<Item> items;

    /* Factory construction */

    /**
     * Factory creation for when there isn't an input ArrayList of songs ready for input.
     * @return an instance of the ItemList class holding no item.
     */
    public static ItemList newInstance() {
        return new ItemList(new ArrayList<Item>());
    }

    /**
     * Factory creation that will bind the ItemList to an ArrayList of Item object that has been
     * pre-initialized.
     * @param items is an ArrayList of the Item object that has been pre-initialized.
     * @return an instance of the ItemList object containing the Item objects that were present in
     * items.
     */
    public static ItemList newInstance(ArrayList<Item> items) {
        return new ItemList(items);
    }

    /**
     * Class constructor. Designed to take in the ArrayList of items for better testing.
     * @param items is an ArrayList of Item objects that the ItemList will hold.
     */
    public ItemList(ArrayList<Item> items) {
        this.items = items;
    }

    /* Setters and Getters */
}
