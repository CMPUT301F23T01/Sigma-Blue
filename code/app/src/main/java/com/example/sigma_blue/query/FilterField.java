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
    private String filterString;

    FilterField(String dbField) {
        this.dbField = dbField;
    }

    FilterField() {
    }

    /**
     * Returns the filter string wrapped in a nullable for proper handling.
     * @return the optional wrapped filter string. if it is not there, then
     * the enum is being used wrong.
     */
    public Optional<String> getFilterString() {
        return Optional.ofNullable(filterString);
    }

    /**
     * This method is used to set the filter string that is being used for
     * certain queries. Restrictions are weak currently
     * @param filterString is the string that is being packaged for testing.
     */
    public void setFilterString(String filterString) {
        if (this == MAKE || this == DESCRIPTION)
            this.filterString = filterString;
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
