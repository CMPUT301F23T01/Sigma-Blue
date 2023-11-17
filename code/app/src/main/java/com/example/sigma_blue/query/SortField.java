package com.example.sigma_blue.query;

import com.example.sigma_blue.entity.item.Item;

import java.util.Comparator;

public enum SortField {
    NAME(Item.dbName),
    DATE(Item.dbDate),
    DESCRIPTION(Item.dbDescription),
    MAKE(Item.dbMake),
    VALUE(Item.dbValue);

    private String dbFieldName;

    private SortField(String dbFieldName) {
        this.dbFieldName = dbFieldName;
    }

    /**
     * Returns the field to sort by on the database
     * @return the field name on that database to sort by
     */
    public String getDbField() {
        return this.dbFieldName;
    }
}
