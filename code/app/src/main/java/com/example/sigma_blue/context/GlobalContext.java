package com.example.sigma_blue.context;

import android.util.Log;

import com.example.sigma_blue.adapter.TabSelected;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.account.AccountList;
import com.example.sigma_blue.entity.description.DescriptionManager;
import com.example.sigma_blue.entity.image.ImageManager;
import com.example.sigma_blue.entity.item.Item;

import com.example.sigma_blue.entity.item.ItemList;

import com.example.sigma_blue.entity.tag.Tag;
import com.example.sigma_blue.entity.tag.TagList;

import java.util.ArrayList;

/**
 * Used for sharing data between activities/fragments instead of bundles. Bundles should only be
 * used when interfacing with 3rd party code.
 * Guidelines:
 *   - ALL state shared between two or more activities/fragments/classes should be here.
 *   - Call newState() right before switching to a new activity/fragment. The new state should be
 *     the name of the state being switched to.
 *   - add "private GlobalContext globalContext" to every class you use global context.
 */
public class GlobalContext {
    private static GlobalContext instance;
    private Account account; // not final to ease testing
    private final AccountList accountList;
    private ItemList itemList;
    private final SelectedEntities<Item> selectedItems;
    private TagList tagList;
    private final SelectedEntities<Tag> selectedTags;
    private final ImageManager imageManager;
    private final DescriptionManager descriptionManager;
    private Item currentItem;
    private Item modifiedItem;
    private TabSelected tabSelected;
    private final ArrayList<ApplicationState> stateHistory; // store a history for debugging

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
        this.tagList.startListening();
        this.itemList = ItemList.newInstance();
        this.itemList.startListening();
        this.tabSelected = TabSelected.Details;
    }

    /**
     * Start everything empty on construction.
     */
    public GlobalContext() {
        this.selectedItems = new SelectedEntities<Item>();
        this.selectedTags = new SelectedEntities<Tag>();
        this.accountList = new AccountList();
        this.stateHistory = new ArrayList<>();
        this.imageManager = new ImageManager();
        this.descriptionManager = new DescriptionManager();
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
    public ImageManager getImageManager() {return imageManager;}

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

    public void updateTag(Tag newTag, Tag oldTag) {
        this.tagList.updateEntity(newTag, oldTag);
        this.selectedTags.updateEntity(newTag, oldTag);
        this.itemList.updateTags(newTag, oldTag);
    }

    public DescriptionManager getDescriptionManager() {
        return descriptionManager;
    }

    public TabSelected getTabSelected() {
        return tabSelected;
    }

    public void setTabSelected(TabSelected tabSelected) {
        this.tabSelected = tabSelected;
    }
}
