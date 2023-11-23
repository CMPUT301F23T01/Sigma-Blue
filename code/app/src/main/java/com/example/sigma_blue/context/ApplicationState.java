package com.example.sigma_blue.context;

import androidx.annotation.NonNull;

public enum ApplicationState {
    SORT_MENU("sort_menu");

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
