package com.dafttech.filterlist;

@SuppressWarnings("unchecked")
public class Blacklist<FilterType> extends Filterlist<FilterType> {

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
