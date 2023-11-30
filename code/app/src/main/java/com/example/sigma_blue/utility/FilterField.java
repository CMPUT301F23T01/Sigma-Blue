package com.example.sigma_blue.utility;

public abstract class FilterField<T> {
    protected String filterText;
    protected boolean enabled;
    protected boolean exact;

    public FilterField(String filterText, boolean enabled, boolean exact) {
        if (filterText == null) {
            this.filterText = "";
        } else {
            this.filterText = filterText.toLowerCase();
        }
        this.enabled = enabled;
        this.exact = exact;
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
    public boolean isEnabled() {
        return enabled;
    }
}
