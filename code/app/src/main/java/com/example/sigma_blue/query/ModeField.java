package com.example.sigma_blue.query;

import androidx.annotation.NonNull;

public enum ModeField {
    SORT("Sort"), FILTER("Filter");

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
