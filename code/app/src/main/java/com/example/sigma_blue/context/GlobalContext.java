package com.example.sigma_blue.context;

import android.util.Log;

import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.item.ItemList;
import com.example.sigma_blue.entity.item.ItemListAdapter;
import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.entity.tag.TagList;

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
        this.tagList = TagList.newInstance(account);
    }

    /**
     * Start the item list (this method could be removed, not sure what other people think).
     * @param itemClickListener
     * @param longClickListener
     */
    public void setUpItemList(ItemListAdapter.OnItemClickListener itemClickListener, ItemListAdapter.OnLongClickListener longClickListener) {
        this.itemList = ItemList.newInstance(this.account, itemClickListener, longClickListener);
        this.itemList.startListening();
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
    public void toggleHighlightItem(Item item) {
        if (!this.highlightedItems.contains(item)){
            this.highlightedItems.add(item);
        } else {
            this.highlightedItems.remove(item);
        }
    }

    /**
     * Return list of highlighted items
     * @return
     */
    public ArrayList<Item> getHighlightedItems() {
        return this.highlightedItems;
    }

    /**
     * Clear highlighted items
     */
    public void resetHighlightedItems() {
        this.highlightedItems.clear();
        this.itemList.getAdapter().resetHighlightedItems();
    }

    /**
     * Toggle if a tag is in the list of highlighted tags or not
     * @param tag
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
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public AccountList getAccountList() {
        return accountList;
    }

    public ItemList getItemList() {
        return itemList;
    }

    public TagList getTagList() {
        return tagList;
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(Item currentItem) {
        this.currentItem = currentItem;
    }
}
