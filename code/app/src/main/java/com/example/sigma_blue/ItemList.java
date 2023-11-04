package com.example.sigma_blue;

import java.util.ArrayList;

public class ItemList implements IAdaptable<Item> {
    /* Attributes */
    private ArrayList<Item> items;
    private ItemDB databaseInterface;

    /* Factory construction */

    /**
     * Factory creation for when there isn't an input ArrayList of songs ready for input.
     * @return an instance of the ItemList class holding no item.
     */
    public static ItemList newInstance() {
        ItemList ret = new ItemList(new ArrayList<Item>());
        ret.setDatabaseInterface(new ItemDB());
        return ret;
    }

    /**
     * Factory creation that will bind the ItemList to an ArrayList of Item object that has been
     * pre-initialized.
     * @param items is an ArrayList of the Item object that has been pre-initialized.
     * @return an instance of the ItemList object containing the Item objects that were present in
     * items.
     */
    public static ItemList newInstance(ArrayList<Item> items) {
        ItemList ret = new ItemList(items);
        ret.setDatabaseInterface(new ItemDB());
        return ret;
    }

    /**
     * Class constructor. Designed to take in the ArrayList of items for better testing.
     * @param items is an ArrayList of Item objects that the ItemList will hold.
     */
    public ItemList(ArrayList<Item> items) {
        this.items = items;
    }

    /* Adapter interface methods */

    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * The getItem method returns the Item object (casted to Object) that is stored in the array
     * list at the position.
     * @param position is the index that the item is being retrieved from.
     * @return the Item stored at the index as an Object object.
     */
    @Override
    public Item getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemId(int position) {
        return position;
    }

    /**
     * Returns the amount of elements held in the items ArrayList.
     * @return an integer primitive representing the number of element held in
     * the items ArrayList.
     */
    public int size() {
        return items.size();
    }

    /* Setters and Getters */

    /**
     * TODO: Handle addition of the exact same object (unique)
     * @param o is the new item being added. If o is null, then it will not be added to the itemList
     */
    public void add(Item o) {
        if (o != null) this.items.add(o);
    }

    /**
     * TODO: Need to handle invalid cases.
     * @param position is the index which is being removed from the list.
     */
    public void remove(int position) {
        this.items.remove(position);
    }


    /* Database method */

    /**
     * Setter used for the factory method and dependency injection.
     * @param databaseHandler is the Database Handler that will be used for the list.
     */
    public void setDatabaseInterface(ItemDB databaseHandler) {
        this.databaseInterface = databaseHandler;
    }

    private void refreshFromDB() {
        this.items = databaseInterface.refreshFromDB();
    }

}
