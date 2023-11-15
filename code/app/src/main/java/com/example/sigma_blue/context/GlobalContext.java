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
 *   - ALL state shared between two or more activities/fragments should be here.
 *   - Call newState() right before switching to a new activity/fragment. The new state should be
 *     the name of the state being switched to.
 *   -
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

    public static GlobalContext getInstance() {
        if (instance == null) {
            instance = new GlobalContext();
        }
        return instance;
    }

    public void login(Account account) {
        this.account = account;
        this.tagList = TagList.newInstance(account);
    }

    public void setUpItemList(ItemListAdapter.OnItemClickListener itemClickListener, ItemListAdapter.OnLongClickListener longClickListener) {
        this.itemList = ItemList.newInstance(this.account, itemClickListener, longClickListener);
        this.itemList.startListening();
    }
    public GlobalContext() {
        this.highlightedItems = new ArrayList<>();
        this.highlightedTags = new ArrayList<>();
        this.accountList = new AccountList();
        this.stateHistory = new ArrayList<>();
    }
    public void toggleHighlightItem(Item item) {
        if (!this.highlightedItems.contains(item)){
            this.highlightedItems.add(item);
        } else {
            this.highlightedItems.remove(item);
        }
    }
    public ArrayList<Item> getHighlightedItems() {
        return this.highlightedItems;
    }
    public void resetHighlightedItems() {
        this.highlightedItems.clear();
        this.itemList.getAdapter().resetHighlightedItems();
    }
    public void toggleHighlightTag(Tag tag) {
        if (!this.highlightedTags.contains(tag)){
            this.highlightedTags.add(tag);
        } else {
            this.highlightedTags.remove(tag);
        }
        this.getTagList().getAdapter().notifyDataSetChanged();
    }
    public ArrayList<Tag> getHighlightedTags() {
        return this.highlightedTags;
    }
    public void resetHighlightedTags() {
        this.highlightedTags.clear();
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
