package com.dafttech.filterlist;

public class Whitelist<FilterType> extends Filterlist<FilterType> {
    public Whitelist(FilterType... filterObjects) {
        super(filterObjects);
    }

    @Override
    public boolean isWhitelist() {
        return true;
    }

    @Override
    public boolean isFiltered(Object object) {
        return isContained(object);
    }

    @Override
    public boolean isValid() {
        return filterObjects.length > 0;
    }
}
