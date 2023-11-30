package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;

import java.util.Objects;

public class DescriptionFilterField extends FilterField<Item>{
    public DescriptionFilterField(String filterText) {
        super(filterText);
    }

    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return Objects.equals(entity.getDescription(), this.getFilterText());
        } else {
            return true;
        }
    }
}