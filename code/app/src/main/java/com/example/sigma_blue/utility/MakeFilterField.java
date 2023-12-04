package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;

/**
 * Check if an item make matches a string
 */
public class MakeFilterField extends FilterField<Item>{
    public MakeFilterField(String filterText, boolean enabled, boolean exact) {
        super(filterText, enabled, exact);
    }

    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return exact ?
                    entity.getMake().toLowerCase().equals(filterText)
                    :
                    entity.getMake().toLowerCase().contains(filterText);
        } else {
            return true;
        }
    }
}
