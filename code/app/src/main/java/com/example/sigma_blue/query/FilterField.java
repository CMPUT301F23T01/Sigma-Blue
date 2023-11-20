package com.example.sigma_blue.query;

import java.util.Optional;

/**
 * This class represents what the application is currently filtering in the list
 * view.
 */
public enum FilterField {
    // The list of filter types that will be supported
    NONE, DATE_RANGE, TAG, DESCRIPTION, MAKE;

    private FilterField() {
    }
}
