package com.example.sigma_blue.query;

import java.time.LocalDate;

/**
 * This is not great OOP. The DateFilterField isn't quite a FilterField (filterText isn't used)
 */
public class DateFilterField extends FilterField{
    LocalDate startDate;
    LocalDate endDate;

    public DateFilterField(LocalDate startDate, LocalDate endDate, Boolean enabled, FilterFieldName type) {
        super(null, type);
        this.startDate = startDate;
        this.endDate = endDate;
        this.enabled = enabled;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
