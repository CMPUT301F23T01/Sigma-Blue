package com.example.sigma_blue.query;

import com.example.sigma_blue.entity.item.Item;

import java.util.Optional;

/**
 * This class represents what the application is currently filtering in the list
 * view.
 */
public enum FilterField {
    // The list of filter types that will be supported
    NONE, DATE_RANGE(Item.dbDate), TAG(Item.dbTags), DESCRIPTION(Item
            .dbDescription), MAKE(Item.dbMake);

    private String dbField;

    FilterField(String dbField) {
        this.dbField = dbField;
    }

    FilterField() {
    }

    /**
     * Returns the dbField for the type
     * @return a String key that is used for the database field. Will return
     * null if the enum is null.
     */
    public String getDbField() {
        if (this != NONE) return this.dbField;
        else throw new IllegalStateException(
                "Trying to filter the NONE filter type");
    }

}
