package com.dafttech.eventmanager;

import java.util.List;

public class EventTypeGetterTest {
    Object universalObj;
    int type = 0;

    protected EventTypeGetterTest(EventType eventType) {
        universalObj = eventType;
        type = 0;
    }

    protected EventTypeGetterTest(int id) {
        universalObj = id;
        type = 1;
    }

    protected EventTypeGetterTest(String name) {
        universalObj = name;
        type = 2;
    }

    protected EventType getFromList(List<EventType> list) {
        if (list.contains(this)) return list.get(list.indexOf(this));
        return null;
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof EventType) {
            if (type == 0) return paramObject == universalObj;
            if (type == 1) return ((EventType) paramObject).id == (Integer) universalObj;
            if (type == 2) return ((EventType) paramObject).name.equals(universalObj);
        }
        return false;
    }
}