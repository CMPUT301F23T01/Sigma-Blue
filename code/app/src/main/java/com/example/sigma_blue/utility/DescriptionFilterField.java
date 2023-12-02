package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

/**
 * Check if an item description matches a string
 */
public class DescriptionFilterField extends FilterField<Item>{
    public DescriptionFilterField(String filterText, boolean enabled, boolean exact) {
        super(filterText, enabled, exact);
    }

    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return exact ?
                    entity.getDescription().toLowerCase().equals(filterText)
                    :
                    entity.getDescription().toLowerCase().contains(filterText);
        } else {
            return true;
        }
    }
}