package com.example.sigma_blue.utility;

import androidx.annotation.NonNull;

/**
 * Modes for the search/filter fragment
 */
public enum ModeField {
    SORT("Sort"), SEARCH("Search");

    final private String stringRepresentation;

    ModeField(final String representation) {
        stringRepresentation = representation;
    }

    @NonNull
    @Override
    public String toString() {
        return stringRepresentation;
    }
}