package com.dafttech.eventmanager;

import java.util.List;

public class EventTypeGetter {
    Object universalObj;
    int mode = 0;

    protected EventTypeGetter(EventType type) {
        universalObj = type;
        mode = 0;
    }

    protected EventTypeGetter(String name) {
        universalObj = name;
        mode = 1;
    }

    protected <T> T getFromList(List<T> list) {
        if (list.contains(this)) return list.get(list.indexOf(this));
        return null;
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof EventType) {
            if (mode == 0) return paramObject == universalObj;
            if (mode == 1) return ((EventType) paramObject).name.equals(universalObj);
        }
        return false;
    }
}