package com.example.sigma_blue.utility;

public abstract class FilterField<T> {
    private String filterText;
    protected boolean enabled;

    public FilterField(String filterText) {
        this.filterText = filterText;
        this.enabled = true;
    }

    /**
     * Does the given entity fit the filter critirion?
     * @param entity entity to check
     * @return true if the entity fits the filter, false otherwise.
     */
    public abstract boolean match(T entity);

    public String getFilterText() {
        return filterText;
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
