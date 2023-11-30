package com.example.sigma_blue.entity.item;

import com.example.sigma_blue.utility.DateFilterField;
import com.example.sigma_blue.utility.DescriptionFilterField;
import com.example.sigma_blue.utility.FilterField;
import com.example.sigma_blue.utility.ItemSortComparator;
import com.example.sigma_blue.utility.MakeFilterField;
import com.example.sigma_blue.utility.NameFilterField;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Comparator;

public class VisibleItemList {
    private FilterField<Item> makeFilterField;
    private FilterField<Item> nameFilterField;
    private FilterField<Item> descriptionFilterField;
    private FilterField<Item> dateFilterField;
    private ItemSortComparator itemSortComparator;
    private final ArrayList<Item> visibleItems;
    private final ArrayList<Item> allItems;
    public VisibleItemList(ArrayList<Item> allItems, ArrayList<Item> visibleItems) {
        this.allItems = allItems;
        this.visibleItems = visibleItems;
        resetVisibleItems();
    }

    public void refreshVisibleItems() {
        visibleItems.clear();
        for (Item i : allItems) {
            if (makeFilterField.match(i) &&
                nameFilterField.match(i) &&
                descriptionFilterField.match(i) &&
                dateFilterField.match(i)){

                visibleItems.add(i);
            }
        }
        this.visibleItems.sort(itemSortComparator.getComparator());
    }
    public void resetVisibleItems() {
        makeFilterField         = new MakeFilterField(null, false, false);
        nameFilterField         = new NameFilterField(null, false, false);
        descriptionFilterField  = new DescriptionFilterField(null, false, false);
        dateFilterField         = new DateFilterField(null, null, false);
        itemSortComparator      = new ItemSortComparator();
        refreshVisibleItems();
    }
    public void setMakeFilterField(FilterField<Item> makeFilterField) {
        this.makeFilterField = makeFilterField;
    }

    public void setNameFilterField(FilterField<Item> nameFilterField) {
        this.nameFilterField = nameFilterField;
    }

    public void setDescriptionFilterField(FilterField<Item> descriptionFilterField) {
        this.descriptionFilterField = descriptionFilterField;
    }

    public void setDateFilterField(FilterField<Item> dateFilterField) {
        this.dateFilterField = dateFilterField;
    }
    public void setItemSortComparator(ItemSortComparator itemSortComparator) {
        this.itemSortComparator = itemSortComparator;
    }

    public FilterField<Item> getMakeFilterField() {
        return makeFilterField;
    }

    public FilterField<Item> getNameFilterField() {
        return nameFilterField;
    }

    public FilterField<Item> getDescriptionFilterField() {
        return descriptionFilterField;
    }

    public FilterField<Item> getDateFilterField() {
        return dateFilterField;
    }

    public ItemSortComparator getItemSortComparator() {
        return itemSortComparator;
    }
}
