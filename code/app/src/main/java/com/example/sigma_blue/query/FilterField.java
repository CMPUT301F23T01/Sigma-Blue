package com.example.sigma_blue.query;

public class FilterField {
    private String filterText;
    private FilterFieldName type;
    protected boolean enabled;

    public FilterField(String filterText, FilterFieldName type) {
        this.filterText = filterText;
        this.type = type;
    }

    public String getFilterText() {
        return filterText;
    }

    public FilterFieldName getType() {
        return type;
    }
    public void enable() {
        enabled = true;
    }
    public void disable() {
        enabled = false;
    }
    public boolean isEnabled() {
        return enabled;
    }
}
