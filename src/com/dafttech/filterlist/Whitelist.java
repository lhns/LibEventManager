package com.dafttech.filterlist;

@SuppressWarnings("unchecked")
public class Whitelist<FilterType> extends Filterlist<FilterType> {

    @Override
    public boolean isWhitelist() {
        return true;
    }

    @Override
    public boolean isFiltered(FilterType object) {
        return isContained(object);
    }

    public boolean isValid() {
        return filterObjects.length > 0;
    }
}
