package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;
import com.google.firebase.firestore.Query;

import java.util.Comparator;

public class ItemSortComparator {
    private SortField sortBy;
    private Query.Direction direction;
    private Comparator<Item> comparator;

    public SortField getSortBy() {
        return sortBy;
    }

    public Comparator<Item> getComparator() {
        return comparator;
    }

    public Query.Direction getDirection() {
        return direction;
    }

    public void setDirection(Query.Direction direction) {
        this.direction = direction;
    }
}
