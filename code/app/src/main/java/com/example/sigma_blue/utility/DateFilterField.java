package com.example.sigma_blue.utility;

import com.example.sigma_blue.entity.item.Item;
import java.util.Date;

/**
 * Check if an item is within a date range
 */
public class DateFilterField extends FilterField<Item> {
    Date startDate;
    Date endDate;

    public DateFilterField(Date startDate, Date endDate) {
        super(null);
        this.startDate = startDate;
        this.endDate = endDate;
    }
    @Override
    public boolean match(Item entity) {
        if (enabled) {
            return entity.getDate().after(startDate) && entity.getDate().before(endDate);
        } else {
            return true;
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }
}
