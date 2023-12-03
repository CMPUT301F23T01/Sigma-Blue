package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;
import com.google.firebase.firestore.Query;

import java.util.Comparator;

/**
 * Store all the comparators used for sorting
 */
public class ItemSortComparator {
    private SortField sortBy;
    private int direction;
    // note that since direction is -1 or 1 it inverts the 'usual' result fo the compareTo method when
    // the direction needs to be reversed.
    private final Comparator<Item> defaultComparator      = (o1, o2) -> direction * o1.getName().compareTo(o2.getName());
    private final Comparator<Item> nameComparator         = (o1, o2) -> direction * o1.getName().compareTo(o2.getName());
    private final Comparator<Item> dateComparator         = (o1, o2) -> direction * o1.getDate().compareTo(o2.getDate());
    private final Comparator<Item> makeComparator         = (o1, o2) -> direction * o1.getMake().compareTo(o2.getMake());
    private final Comparator<Item> valueComparator        = (o1, o2) -> direction * o1.getValue().compareTo(o2.getValue());
    private final Comparator<Item> descriptionComparator  = (o1, o2) -> direction * o1.getDescription().compareTo(o2.getDescription());

    public ItemSortComparator(SortField sortBy, int direction){
        this.sortBy = sortBy;
        this.direction  = direction;
    }
    public ItemSortComparator( ){
        this.sortBy = SortField.NO_SELECTION;
        direction  = 1;
    }

    /**
     * Get the type of sorting being done currently
     * @return
     */
    public SortField getSortBy() {
        return sortBy;
    }

    /**
     * Returns the comparator for the current sorting mode
     * @return
     */
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

    /**
     * returns an int representing the current sorting direction. 1 for ascending, -1 for descending
     * @return
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Set the direction of sorting. 1 for ascending, -1 for descending
     * @param direction
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }
}