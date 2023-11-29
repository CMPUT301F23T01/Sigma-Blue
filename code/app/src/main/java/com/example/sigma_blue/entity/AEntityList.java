package com.example.sigma_blue.entity;

import android.util.Log;

import com.example.sigma_blue.adapter.ASelectableListAdapter;
import com.example.sigma_blue.context.GlobalContext;
import com.example.sigma_blue.database.ADatabaseHandler;
import com.example.sigma_blue.database.IDatabaseItem;
import com.example.sigma_blue.database.IDatabaseList;
import com.example.sigma_blue.entity.account.Account;
import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.tag.Tag;
import com.google.firebase.firestore.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AEntityList<T> implements Serializable, IDatabaseList<T>{
    protected ArrayList<T> entityList;
    protected ADatabaseHandler<T> dbHandler;
    protected ASelectableListAdapter<T> adapter;
    protected GlobalContext globalContext;
    /**
     * Both updates the list held in this class and the adapter element.
     * @param list is the list that is replacing the current list.
     */
    public void setList(final ArrayList<T> list) {
        this.entityList = list;
        this.adapter.setList(list);
    }

    public ArrayList<T> getList() {
        return this.entityList;
    }
    public ASelectableListAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(ASelectableListAdapter<T> adapter) {
        this.adapter = adapter;
        this.adapter.setList(this.entityList);
        this.adapter.notifyDataSetChanged();
    }

    public ArrayList<T> getEntityList() {
        return entityList;
    }

    /**
     * Add an entity to the list
     * @param o is the new item being added. If o is null, then it will not be added to the itemList
     */
    public void add(T o) {
        if (o != null) {
            this.entityList.add(o);
            this.dbHandler.add((IDatabaseItem<T>) o);
        }
        updateUI();
    }

    public abstract void updateUI();

    /**
     * @param position is the index which is being removed from the list.
     */
    public void remove(final int position) {
        if (position > -1 && position < entityList.size()) {
            this.dbHandler.remove((IDatabaseItem<T>) entityList.get(position));
            this.entityList.remove(position);
        } else {
            Log.e("Remove Item",
                    "Invalid remove action: negative index or index out of range");
        }
        updateUI();
        Log.v("Removed Item", "Removed an item from item list");
    }

    /**
     * Delete the item from the Database
     * @param deletedEntity the item to be deleted
     */
    public void remove(T deletedEntity) {
        for (int i  = 0; i < this.entityList.size(); i++) {
            if (entityList.get(i).equals(deletedEntity)) {
                this.remove(i);
            }
        }
        updateUI();
    }

    /**
     * Sets up a listening thread for changes to the database collection. This
     * method will update the state of the list.
     */
    public void startListening() {
        dbHandler.startListening(this.dbHandler.getCollectionReference(), (IDatabaseList<T>) this);
    }

    /**
     * Same as above, but with a query.
     * @param currentQuery query to use for filtering
     */
    public void startListening(Query currentQuery) {
        this.dbHandler.startListening(currentQuery, (IDatabaseList<T>) this);
    }

    /**
     * Replace the old entity with a new one
     * @param newE replacement entity
     * @param oldE entity to replace
     */
    public void updateEntity(T newE, T oldE) {
        this.entityList.remove(oldE);
        this.entityList.add(newE);
        this.dbHandler.remove((IDatabaseItem<T>) oldE);
        this.dbHandler.add((IDatabaseItem<T>) newE);
        updateUI();
    }

    /**
     * Resynchronizes the item with its DB equivalent. Same as updateEntity(i, i);
     * @return
     */
    public void syncEntity(T e){
        this.dbHandler.remove((IDatabaseItem<T>) e);
        this.dbHandler.add((IDatabaseItem<T>) e);
    }

    public ADatabaseHandler<T> getDbHandler() {
        return dbHandler;
    }

    public void setDbHandler(ADatabaseHandler<T> dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * Default instance creation. Should use the account in global context (if account needed).
     * This method should also set up the db handler and adapter.
     */
    //public abstract AEntityList<T> newInstance();


    /**
     * This method removes all the items owned by the user. Made for testing.
     */
    public void removeAll() {
        this.entityList.stream().forEach(item -> dbHandler.remove((IDatabaseItem<T>) item));
        this.entityList.clear();
    }
}
