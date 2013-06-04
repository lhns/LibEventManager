package com.dafttech.eventmanager;

public class EventListenerContainer {
    public volatile Object eventlistener = null;
    volatile protected int priority = 0;
    volatile protected Object[] filter = null;

    public EventListenerContainer(Object eventlistener, int priority, Object[] filter) {
        this.eventlistener = eventlistener;
        this.priority = priority;
        this.filter = filter;
    }
}