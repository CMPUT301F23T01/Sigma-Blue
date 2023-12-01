package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

public class NameFilterField extends FilterField<Item>{
    public NameFilterField(String filterText, boolean enabled, boolean exact) {
        super(filterText, enabled, exact);
    }

    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return exact ?
                    entity.getName().toLowerCase().equals(filterText)
                    :
                    entity.getName().toLowerCase().contains(filterText);
        } else {
            return true;
        }
    }
}