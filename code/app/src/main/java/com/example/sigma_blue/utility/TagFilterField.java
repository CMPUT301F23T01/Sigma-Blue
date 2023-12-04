package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;
import com.example.sigma_blue.entity.tag.Tag;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

/**
 * Check if an item's tags matches a given list of tags
 */
public class TagFilterField extends FilterField<Item>{
    private final ArrayList<Tag> tagsToMatch;

    /**
     *
     * @param filterText not used for this class
     * @param enabled if disabled always match true
     * @param exact if true all tags must match, if false any tag must match
     */
    public TagFilterField(String filterText, boolean enabled, boolean exact, ArrayList<Tag> tagsToMatch) {
        super(filterText, enabled, exact);
        this.tagsToMatch = tagsToMatch;
    }

    @Override
    public boolean match(Item entity) {
        if (!enabled) {
            return true;
        }
        boolean match;
        if(exact) {
            match = true;
            for (Tag t : entity.getTags()) {
                if (!tagsToMatch.contains(t)) {
                    match = false;
                    break;
                }
            }
        } else {
            match = false;
            for (Tag t : entity.getTags()) {
                if (tagsToMatch.contains(t)) {
                    match = true;
                    break;
                }
            }
        }
        return match;
    }
}
