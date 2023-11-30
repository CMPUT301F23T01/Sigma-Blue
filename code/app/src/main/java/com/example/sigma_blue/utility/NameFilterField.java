package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

public class NameFilterField extends FilterField<Item>{
    public NameFilterField(String filterText) {
        super(filterText);
    }

    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return Objects.equals(entity.getName(), this.getFilterText());
        } else {
            return true;
        }
    }
}