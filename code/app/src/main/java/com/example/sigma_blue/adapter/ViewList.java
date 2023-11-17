package com.example.sigma_blue.adapter;

import android.icu.number.Precision;

import com.example.sigma_blue.entity.item.Item;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * The goal of this class is to be able to feed it the raw input of the database
 * and a mode of operation, along with a comparator or a predicate, and then
 * display a filtered or sorted view
 */
public class ViewList {
    /**
     * Enum for mode determination
     */
    public enum ViewListModes {
        NONE, SORT, FILTER;

        private ViewListModes() {
        }
    }
    private ViewListModes currentMode;
    private List<? extends Item> baseTruth;
    private List<? extends Item> viewList;  // This is what is shown to the user

    // Conditions for filtering and sorting.
    private Predicate<? super Item> predicate;
    private Comparator<? extends Item> comparator;

    /**
     * Base constructor sets the mode to NONE, which means no sorting or
     * filtering
     */
    public ViewList() {
        currentMode = ViewListModes.NONE;
    }

    public void setMode(ViewListModes mode) {
        this.currentMode = mode;
    }

    public void resetMode() {
        this.currentMode = ViewListModes.NONE;
    }

    public List<? extends Item> getViewList() {
        return viewList;
    }
}
