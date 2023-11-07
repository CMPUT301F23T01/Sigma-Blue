package com.example.sigma_blue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemList implements IAdaptable<Item> {
    /* Attributes */
    private List<Item> items;

    /* Factory construction */

    /**
     * Factory creation for when there isn't an input ArrayList of songs ready for input.
     * @return an instance of the ItemList class holding no item.
     */
    public static ItemList newInstance() {
        ItemList ret = new ItemList(new ArrayList<Item>());
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

    /**
     * Gets the sum of all the value of the item in the ItemList.
     * @author Bach
     * @return the sum of the values of the items contained in this instance in
     * an Optional wrapper. Done this way to enforce explicit handling of the
     * case where there is no items in the list.
     */
    public Optional<Float> sumValues() {
        if (items.isEmpty()) return Optional.empty();
        else return Optional.of(items.stream().map(Item::getValue)
                    .reduce(0f, Float::sum));
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

    public void setList(final List<Item> list) {
        this.items = list;
    }

/*    private void refreshFromDB() {
        this.items = databaseInterface.refreshFromDB();
    }*/

}
