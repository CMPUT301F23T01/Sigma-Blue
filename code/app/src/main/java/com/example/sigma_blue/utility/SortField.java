package com.example.sigma_blue.utility;

import androidx.annotation.NonNull;

import com.example.sigma_blue.entity.item.Item;

import java.util.Comparator;

/**
 * Enum of the ways to sort an item
 */
public enum SortField {
    NAME("Name"),
    DATE("Date"),
    DESCRIPTION("Description"),
    MAKE("Make"),
    VALUE("Item Value"),
    TAG("Tag"),
    NO_SELECTION("No Selection");

    final private String menuName;

    SortField(String menuName) {
        this.menuName = menuName;
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