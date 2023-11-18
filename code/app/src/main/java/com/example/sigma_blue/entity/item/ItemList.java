package com.example.sigma_blue.entity.item;

import android.util.Log;
import android.widget.TextView;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.adapter.IAdaptable;
import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.query.QueryGenerator;
import com.example.sigma_blue.query.SortField;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ItemList implements IAdaptable<Item>, IDatabaseList<Item> {
    /* Attributes */
    private List<Item> items;
    private ItemDB dbHandler;

    private ItemListAdapter listAdapter;
    private ViewListModes displayMode;


    public enum ViewListModes {
        NONE, SORT, FILTER;

        private ViewListModes() {
        }
    }

    /* Factory construction */

    public static ItemList newInstance(Account a, ItemDB dbH,
                                       ItemListAdapter adapt,
                                       List<? extends Item> selectedList) {
        ItemList ret = new ItemList(new ArrayList<>(), a);
        ret.setDatabaseHandler(dbH);
        ret.setListAdapter(adapt, selectedList);
        return ret;
    }

    public static ItemList newInstance(Account a, ItemDB dbH) {
        ItemList ret = new ItemList(new ArrayList<>(), a);
        ret.setDatabaseHandler(dbH);
        return ret;
    }

    /**
     * Class constructor. Designed to take in the ArrayList of items for better testing.
     * @param items is an ArrayList of Item objects that the ItemList will hold.
     */
    public ItemList(ArrayList<Item> items, Account account) {
        this.items = items;
        this.displayMode = ViewListModes.NONE;
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
    public static final Function<List<Item>, Optional<Double>> sumValues =
        lst -> {
            if (lst.isEmpty()) return Optional.empty();
            else return Optional.of(lst.stream().map(Item::getValue)
                    .reduce(0d, Double::sum));
        };

    /* Setters and Getters */

    /**
     * TODO: Handle addition of the exact same object (unique)
     * @param o is the new item being added. If o is null, then it will not be added to the itemList
     */
    public void add(Item o) {
        if (o != null) {
            this.items.add(o);
            this.dbHandler.add(o);
        }
        updateUI();
    }

    /**
     * @param position is the index which is being removed from the list.
     */
    public void remove(final int position) {
        if (position > -1 && position < size()) {
            this.dbHandler.remove(items.get(position));
            this.items.remove(position);
        } else {
            Log.e("Remove Item",
                    "Invalid remove action: negative index or index out of range");
        }
        updateUI();
        Log.v("Removed Item", "Removed an item from item list");
    }

    /**
     * Delete the item from the Database
     * @param deletedItem the item to be deleted
     */
    public void remove(Item deletedItem) {
        for (int i = 0; i < this.items.size(); i++) {
            if (Objects.equals(this.items.get(i).getDocID(), deletedItem.getDocID())) {
                this.remove(i);
            }
        }
        updateUI();
    }

    /**
     * This method removes all the items owned by the user. Made for testing.
     */
    public void removeAll() {
        this.items.stream()
                .forEach(item -> dbHandler.remove(item));
        this.items.clear();
    }

    /**
     * Updates the UI to match the current data in the ItemList.
     */
    public void updateUI() {
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
            listAdapter.notifySumView(sumValues.apply(this.items));
        };
    }

    /**
     * Method that will just return an Item List implementation
     * @param q is a QuerySnapshot that is being converted into a list.
     * @return a list of items.
     */
    @Override
    public List<Item> loadArray(QuerySnapshot q) {
        return ItemDB.loadArray(q, Item.itemOfQueryDocument);
    }

    public void setDatabaseHandler(final ItemDB dbH) {
        this.dbHandler = dbH;
    }

    /**
     * Starts listening to the database. Will update the content of this class,
     * and the adapter on change in the database.
     */
    public void startListening() {
        dbHandler.startListening(this.dbHandler.getCollectionReference(),
                this);
    }

    /**
     * The first prototype method for listening to the database via sorting
     * @param sortBy
     * @param direction
     */
    public void startQueryListening(SortField sortBy,
                                    Query.Direction direction) {
        Query query = new QueryGenerator(sortBy, direction,
                this.dbHandler.getCollectionReference()).get();
        dbHandler.startListening(query, this);
    }

    /**
     * Sets the list adapter and does a refresh.
     * @param listAdapter
     */
    public void setListAdapter(final ItemListAdapter listAdapter,
                               final List<? extends Item> selectedList) {
        this.listAdapter = listAdapter;
        this.listAdapter.setSelectedItemList(selectedList);
        if (this.items != null) {
            this.listAdapter.setItemList(this.items);
            this.listAdapter.notifyDataSetChanged();
        }
        this.startListening();
    }

    public ItemListAdapter getListAdapter() {
        return this.listAdapter;

    }

    /* Database method */

    /**
     * Both updates the list held in this class and the adapter element.
     * @param list is the list that is replacing the current list.
     */
    public void setList(final List<Item> list) {
        this.items = list;
        this.listAdapter.setItemList(list);
    }

    public List<Item> getList() {
        return this.items;
    }

    public void setSummaryView(TextView summaryView) {
        this.listAdapter.setSummaryView(summaryView);
        this.listAdapter.notifySumView(sumValues.apply(this.items));
    }

    /**
     * Swaps out an item for a new one.
     * @param updatedItem New Item to put in the list
     * @param oldItem Search for an item with this DocID to replace
     */
    public void updateItem(Item updatedItem, Item oldItem) {
        this.items.remove(oldItem);
        this.items.add(updatedItem);
        updateUI();
    }
}
