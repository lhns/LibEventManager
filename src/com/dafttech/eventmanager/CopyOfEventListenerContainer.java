package com.dafttech.eventmanager;

import java.lang.reflect.Method;

public class CopyOfEventListenerContainer {
    volatile protected Object eventListener = null;
    volatile protected Method method = null;
    volatile protected int priority = 0;
    volatile protected Object[] filter = null;

    protected CopyOfEventListenerContainer(Object eventListener, int priority, Object[] filter) {
        this.eventListener = eventListener;
        this.priority = priority;
        this.filter = filter;
    }

    protected CopyOfEventListenerContainer(Object eventListener, Method method, int priority, Object[] filter) {
        this.eventListener = eventListener;
        this.method = method;
        this.priority = priority;
        this.filter = filter;
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof CopyOfEventListenerContainer) {
            return paramObject == this;
        } else {
            return paramObject == eventListener;
        }
    }
}