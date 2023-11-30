package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;
import com.google.firebase.firestore.Query;

import java.util.Comparator;

public class ItemSortComparator {
    private SortField sortBy;
    private int direction;
    private final Comparator<Item> defaultComparator      = (o1, o2) -> direction * o1.getName().compareTo(o2.getName());
    private final Comparator<Item> nameComparator         = (o1, o2) -> direction * o1.getName().compareTo(o2.getName());
    private final Comparator<Item> dateComparator         = (o1, o2) -> direction * o1.getDate().compareTo(o2.getDate());
    private final Comparator<Item> makeComparator         = (o1, o2) -> direction * o1.getMake().compareTo(o2.getMake());
    private final Comparator<Item> valueComparator        = (o1, o2) -> direction * o1.getValue().compareTo(o2.getValue());
    private final Comparator<Item> descriptionComparator  = (o1, o2) -> direction * o1.getDescription().compareTo(o2.getDescription());

    public ItemSortComparator(){
        sortBy = SortField.NO_SELECTION; // default sort
        direction  = 1;
    }
    public SortField getSortBy() {
        return sortBy;
    }

    public Comparator<Item> getComparator() {
        switch (sortBy) {
            case NAME:
                return nameComparator;
            case DATE:
                return dateComparator;
            case MAKE:
                return makeComparator;
            case VALUE:
                return valueComparator;
            case DESCRIPTION:
                return descriptionComparator;
            default:
                return defaultComparator;
        }
    }
    public int getDirection() {
        return direction;
    }
    public void setDirection(int direction) {
        this.direction = direction;
    }
}