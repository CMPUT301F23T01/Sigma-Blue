package com.example.sigma_blue.context;

import android.util.Log;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.entity.item.Item;

import com.example.sigma_blue.entity.item.ItemDB;
import com.example.sigma_blue.entity.item.ItemList;

import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.entity.tag.TagList;
import com.example.sigma_blue.query.QueryGenerator;
import com.example.sigma_blue.query.QueryMode;
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
    private ArrayList<Item> highlightedItems;
    private TagList tagList;
    private ArrayList<Tag> highlightedTags;
    private Item currentItem;
    private Tag currentTag;
    private QueryMode queryState;

    private ArrayList<String> stateHistory; // store a history for debugging

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
        this.highlightedItems = new ArrayList<>();
        this.highlightedTags = new ArrayList<>();
        this.accountList = new AccountList();
        this.stateHistory = new ArrayList<>();
    }

    /**
     * Toggle if an item is in the list of highlighted items or not.
     * @param item
     */
    public void toggleInsertSelectedItem(Item item) {

        if (!this.highlightedItems.contains(item)){
            this.highlightedItems.add(item);
        } else {
            this.highlightedItems.remove(item);
        }

        this.getItemList().getAdapter().notifyDataSetChanged();
    }

    /**
     * Return list of highlighted items
     * @return a List of the selected item
     */
    public ArrayList<Item> getSelectedItems() {
        return this.highlightedItems;
    }

    /**
     * Clear highlighted items
     */
    public void resetSelectedItems() {
        this.highlightedItems.clear();
    }

    /**
     * Method for doing a set difference of the items stored and the selected
     * items.
     */
    public void deleteSelectedItems() {
        for (Item i : this.getSelectedItems()) {
            this.getItemList().remove(i);
        }
        this.resetSelectedItems();
        this.getItemList().getAdapter().notifyDataSetChanged();
    }

    /**
     * Toggle if a tag is in the list of highlighted tags or not
     * @param tag is the tag object that has been selected
     */
    public void toggleHighlightTag(Tag tag) {
        if (!this.highlightedTags.contains(tag)){
            this.highlightedTags.add(tag);
        } else {
            this.highlightedTags.remove(tag);
        }
        this.getTagList().getAdapter().notifyDataSetChanged();
    }

    /**
     * Return list of highlighted tags
     * @return
     */
    public ArrayList<Tag> getHighlightedTags() {
        return this.highlightedTags;
    }

    /**
     * Clear list of highlighted tags
     */
    public void resetHighlightedTags() {
        this.highlightedTags.clear();
        this.getTagList().getAdapter().notifyDataSetChanged();
    }

    /**
     * Switch to a new state. This doesn't actually start any activities/fragments, only updates the
     * state history.
     * valid
     * @param state state to switch to
     */
    public void newState(String state) {
        if (stateHistory.size() == 256) { // keep the last 256 states for debugging
            stateHistory.remove(0);
        }
        // TODO make this an enum
        Log.d("STATE_CHANGE", state);
        stateHistory.add(state);
    }
    public String getCurrentState() {
        return stateHistory.get(stateHistory.size() - 1);
    }
    public String getLastState() {
        return stateHistory.get(stateHistory.size() - 2);
    }
    public AccountList getAccountList() {
        return accountList;
    }
    public ItemList getItemList() {
        return itemList;
    }

    /**
     * Setter for the query mode.
     * @return the query mode object, which keeps track of the current query
     * state.
     */
    public QueryMode getQueryState() {
        if (queryState == null) queryState = new QueryMode();   // lazy cons
       return this.queryState;
    }

    public TagList getTagList() {
        return tagList;
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
}
