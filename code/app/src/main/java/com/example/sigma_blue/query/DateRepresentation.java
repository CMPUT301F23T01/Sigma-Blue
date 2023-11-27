package com.example.sigma_blue.query;

import androidx.annotation.NonNull;

public class DateRepresentation {
    final int year;
    final int month;
    final int day;


    public DateRepresentation(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    /**
     * The string conversion method for running the query.
     * @return the string representation of the date.
     */
    @NonNull
    @Override
    public String toString() {
        return year + "-" + (month + 1) + "-" + day;
    }
}
