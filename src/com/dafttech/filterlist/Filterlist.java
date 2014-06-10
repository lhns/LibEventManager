package com.dafttech.filterlist;

public abstract class Filterlist<FilterType> {
    protected FilterType[] filterObjects;

    @SafeVarargs
    public Filterlist(FilterType... filterObjects) {
        this.filterObjects = filterObjects;
        fixArray();
    }

    @SuppressWarnings("unchecked")
    private final void fixArray() {
        if (filterObjects == null) filterObjects = (FilterType[]) new Object[0];
    }

    public abstract boolean isWhitelist();

    public FilterType[] getFilterObjects() {
        return filterObjects;
    }

    public boolean isContained(Object object) {
        for (FilterType obj : filterObjects)
            if (obj == object || obj.equals(object)) return true;
        return false;
    }

    public abstract boolean isFiltered(Object object);

    public abstract boolean isValid();
}
