package com.example.sigma_blue.utility;

import androidx.annotation.NonNull;

import com.example.sigma_blue.entity.item.Item;

import java.util.Comparator;

public enum SortField {
    NAME(Item.dbName, "Name"),
    DATE(Item.dbDate, "Date"),
    DESCRIPTION(Item.dbDescription, "Description"),
    MAKE(Item.dbMake, "Make"),
    VALUE(Item.dbValue, "Item Value"),
    NO_SELECTION(Item.dbName, "No Selection");

    final private String dbFieldName;
    final private String menuName;

    SortField(String dbFieldName, String menuName) {
        this.dbFieldName = dbFieldName;
        this.menuName = menuName;
    }

    /**
     * Returns the field to sort by on the database
     * @return the field name on that database to sort by
     */
    public String getDbField() {
        return this.dbFieldName;
    }

    /**
     * This method makes the enum show up nicely on menus and things that will
     * display it.
     * @return the string that describe the enum. Used in menus.
     */
    @NonNull
    @Override
    public String toString() {
        return this.menuName;
    }
}