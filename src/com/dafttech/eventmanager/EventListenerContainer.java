package com.dafttech.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EventListenerContainer {
    volatile protected Object eventListener = null;
    volatile protected Method eventListenerMethod = null;
    volatile protected int priority = 0;
    
    volatile private Method eventFilterMethod = null;
    @Deprecated
    volatile protected Object[] filter = null;

    @Deprecated
    protected EventListenerContainer(Object eventListener, int priority, Object[] filter) {
        this.eventListener = eventListener;
        this.priority = priority;
        this.filter = filter;
    }

    @Deprecated
    protected EventListenerContainer(Object eventListener, Method method, int priority, Object[] filter) {
        this.eventListener = Modifier.isStatic(method.getModifiers()) ? null : eventListener;
        this.eventListenerMethod = method;
        this.priority = priority;
        this.filter = filter;
    }
    
    protected EventListenerContainer(Object eventListener, Method eventListenerMethod, int priority, Method eventFilterMethod) {
        this.eventListener = Modifier.isStatic(eventListenerMethod.getModifiers()) ? null : eventListener;
        this.eventListenerMethod = eventListenerMethod;
        this.priority = priority;
        this.eventFilterMethod = eventFilterMethod;
    }
    
    protected Object[] getFilter() {
        try {
            if (eventFilterMethod != null) return (Object[]) eventFilterMethod.invoke(eventListener);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return new Object[0];
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof EventListenerContainer) {
            return paramObject == this;
        } else {
            return paramObject == eventListener;
        }
    }
}