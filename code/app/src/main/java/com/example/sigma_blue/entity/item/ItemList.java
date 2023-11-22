package com.example.sigma_blue.entity.item;

import android.util.Log;
import android.widget.TextView;

import com.example.sigma_blue.adapter.ASelectableListAdapter;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.entity.AEntityList;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.database.IDatabaseList;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ItemList extends AEntityList<Item> {
    /* Attributes */
    private ViewListModes displayMode;


    public enum ViewListModes {
        NONE, SORT, FILTER;

        private ViewListModes() {
        }
    }

    /* Factory construction */

    public static ItemList newInstance(ItemDB dbH,
                                       ItemListAdapter adapt) {
        ItemList ret = new ItemList();
        ret.setDbHandler(dbH);
        ret.setAdapter(adapt);

        return ret;
    }

    public static ItemList newInstance(ItemDB dbH) {
        ItemList ret = new ItemList();
        ret.setDbHandler(dbH);
        return ret;
    }

    public static ItemList newInstance() {
        return new ItemList();
    }

    /**
     * Class constructor.
     */
    private ItemList() {
        this.entityList = new ArrayList<Item>();
        this.displayMode = ViewListModes.NONE;
        this.globalContext = GlobalContext.getInstance();
        this.dbHandler = ItemDB.newInstance(globalContext.getAccount());
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


    /**
     * This method removes all the items owned by the user. Made for testing.
     */
    public void removeAll() {
        this.entityList.stream()
                .forEach(item -> dbHandler.remove(item));
        this.entityList.clear();
    }

    /**
     * Updates the UI to match the current data in the ItemList.
     */
    @Override
    public void updateUI() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            ((ItemListAdapter) adapter).notifySumView(sumValues.apply(this.entityList));
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

    /**
     * Needed when making new queries
     * @return the collection reference of the user
     */
    public CollectionReference getCollectionReference() {
        return dbHandler.getCollectionReference();
    }

    public void setSummaryView(TextView summaryView) {
        ((ItemListAdapter) this.adapter).setSummaryView(summaryView);
        ((ItemListAdapter) this.adapter).notifySumView(sumValues.apply(this.entityList));
    }

    /**
     * Clean all the tags in all stored items
     */
    public void cleanAllItemTags() {
        for (Item i : this.entityList) {
            i.cleanTags();
            syncEntity(i);
        }
    }
}
