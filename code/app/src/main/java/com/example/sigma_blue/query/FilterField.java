package com.example.sigma_blue.query;

public class FilterField {
    private String name;
    private FilterFieldName type;

    public FilterField(String name, FilterFieldName type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public FilterFieldName getType() {
        return type;
    }
}
