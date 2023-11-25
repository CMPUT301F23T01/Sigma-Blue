package com.example.sigma_blue.context;

import android.util.Log;

import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.entity.image.ImageDB;
import com.example.sigma_blue.entity.image.ImageList;
import com.example.sigma_blue.entity.item.Item;

import com.example.sigma_blue.entity.item.ItemDB;
import com.example.sigma_blue.entity.item.ItemList;

import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.entity.tag.TagList;
import com.example.sigma_blue.query.QueryGenerator;
import com.example.sigma_blue.query.QueryMode;
import com.example.sigma_blue.utility.Pair;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

/**
 * Guidelines:
 *   - ALL state shared between two or more activities/fragments/classes should be here.
 *   - Call newState() right before switching to a new activity/fragment. The new state should be
 *     the name of the state being switched to. Eventually I'll make an enum for the states.
 *   - add "private GlobalContext globalContext" to every class you use global context.
 */
public class GlobalContext {
    private static GlobalContext instance;
    private Account account;
    private AccountList accountList;
    private ItemList itemList;
    private SelectedEntities<Item> selectedItems;
    private TagList tagList;
    private SelectedEntities<Tag> selectedTags;
    private ImageList imageList;
    private Item currentItem;
    private Item modifiedItem;
    private Tag currentTag;
    private QueryMode queryState;

    private ArrayList<ApplicationState> stateHistory; // store a history for debugging

    /**
     * The intended way to use this class. Singleton design pattern.
     * @return the global context
     */
    public static GlobalContext getInstance() {
        if (instance == null) {
            instance = new GlobalContext();
        }
        return instance;
    }

    /**
     * Set the current account and start the tag list.
     * @param account account to log in as
     */
    public void login(Account account) {
        this.account = account;
        this.tagList = TagList.newInstance();
        this.itemList = ItemList.newInstance();
    }

    /**
     * Start everything empty on construction.
     */
    public GlobalContext() {
        this.selectedItems = new SelectedEntities<Item>();
        this.selectedTags = new SelectedEntities<Tag>();
        this.accountList = new AccountList();
        this.stateHistory = new ArrayList<>();
        this.imageList = new ImageList();
    }

    /**
     * Switch to a new state. This doesn't actually start any activities/fragments, only updates the
     * state history.
     * valid
     * @param state state to switch to
     */
    public void newState(ApplicationState state) {
        if (stateHistory.size() == 256) { // keep the last 256 states for debugging
            stateHistory.remove(0);
        }
        // TODO make this an enum
        Log.d("STATE_CHANGE", state.toString());
        stateHistory.add(state);
    }
    public ApplicationState getCurrentState() {
        if (stateHistory.size() >= 1) {
            return stateHistory.get(stateHistory.size() - 1);
        } else {
            return ApplicationState.NONE;
        }
    }
    public ApplicationState getLastState() {
        return stateHistory.get(stateHistory.size() - 2);
    }
    public AccountList getAccountList() {
        return accountList;
    }

    public ItemList getItemList() {
        return itemList;
    }
    public ImageList getImageList() {return imageList;}
    /**
     * Setter for the query mode.
     * @return the query mode object, which keeps track of the current query
     * state.
     */
    public QueryMode getQueryState() {
        if (queryState == null) queryState = new QueryMode(itemList
                .getCollectionReference());   // lazy cons
       return this.queryState;
    }

    public TagList getTagList() {
        return tagList;
    }

    /**
     * Returns a pair to reduce how verbose the methods are getting.
     * @return a pair of the database list and the database handler. Used for
     * querying.
     */
    public Pair<ADatabaseHandler<Item>, IDatabaseList<Item>> getQueryPair() {
        return new Pair<>(itemList.getDbHandler(), itemList);
    }

    /**
     * Get the Item that is being viewed by the detailed view page.
     * @return the Item object that is being viewed
     */
    public Item getCurrentItem() {
        return currentItem;
    }
    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }

    public SelectedEntities<Item> getSelectedItems() {
        return selectedItems;
    }
    public SelectedEntities<Tag> getSelectedTags() {
        return selectedTags;
    }

    public Item getModifiedItem() {
        return modifiedItem;
    }

    public void setModifiedItem(Item modifiedItem) {
        this.modifiedItem = modifiedItem;
    }
}
