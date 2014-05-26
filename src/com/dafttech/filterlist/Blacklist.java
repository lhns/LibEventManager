package com.dafttech.filterlist;

public class Blacklist<FilterType> extends Filterlist<FilterType> {
    public Blacklist(FilterType... filterObjects) {
        super(filterObjects);
    }

    @Override
    public boolean isWhitelist() {
        return false;
    }

    @Override
    public boolean isFiltered(FilterType object) {
        return !isContained(object);
    }

    @Override
    public boolean isValid() {
        return true;
    }

}
