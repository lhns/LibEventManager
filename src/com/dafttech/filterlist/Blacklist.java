package com.dafttech.filterlist;

public class Blacklist<FilterType> extends Filterlist<FilterType> {
    @SafeVarargs
    public Blacklist(FilterType... filterObjects) {
        super(filterObjects);
    }

    @Override
    public boolean isWhitelist() {
        return false;
    }

    @Override
    public boolean isFiltered(Object object) {
        return !isContained(object);
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
