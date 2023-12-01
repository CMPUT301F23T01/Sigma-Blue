package com.example.sigma_blue.context;

import androidx.annotation.NonNull;

public enum ApplicationState {
    SORT_MENU("sort_menu"), LOGIN_ACTIVITY("login_activity"),
    VIEW_LIST_ACTIVITY("view list activity"),
    CREATE_ACCOUNT_FRAGMENT("create account fragment"),
    LOGIN_FRAGMENT("login fragment"),
    DETAILS_FRAGMENT("details fragment"),
    ADD_ITEM_FRAGMENT("add item fragment"),
    EDIT_ITEM_FRAGMENT("edit item fragment"),
    MULTI_SELECT_TAG_MANAGER_FRAGMENT("multi select tag manager"),
    TAG_MANAGER_FRAGMENT("tag manager fragment"),
    TAG_ADD_FRAGMENT("tag add fragment"),
    TAG_EDIT_FRAGMENT("tag edit fragment"),
    IMAGE_ADD_ACTIVITY("image add activity"),
    BARCODE_ADD_ACTIVITY("barcode add activity"),
    GALLERY_ADD_ACTIVITY("gallery add activity"),
    NONE("none");

    private String key;

    /**
     * The enums need to hold a string key for backward compatibility
     * @param key is the string key that was used in the older version of the
     *            system.
     */
    private ApplicationState(String key) {
        this.key = key;
    }

    /**
     * Needed for efficient backwards compatibility
     * @return the string representation of the enum
     */
    @NonNull
    @Override
    public String toString() {
        return this.key;
    }
}
