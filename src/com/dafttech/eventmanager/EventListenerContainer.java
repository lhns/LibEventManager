package com.dafttech.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EventListenerContainer {
    volatile protected Object eventListener;
    volatile protected boolean eventListenerStatic;
    volatile protected Method method;
    volatile protected int priority;

    volatile private Method filter;

    protected EventListenerContainer(Object eventListener, Method method, EventListener annotation) {
        this.eventListener = eventListener;
        this.eventListenerStatic = Modifier.isStatic(method.getModifiers());
        this.method = method;
        this.priority = annotation.priority();
        this.filter = getFilterMethod(eventListener, annotation.filter());
        if (eventListenerStatic && !(eventListener instanceof Class)) {
            this.eventListener = this.eventListener.getClass();
        }
    }

    protected Object[] getFilter() {
        try {
            if (filter != null) return (Object[]) filter.invoke(eventListener);
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

    private static final Method getFilterMethod(Object eventListener, String filterName) {
        if (!filterName.equals("")) {
            for (Method method : EventManager.getAnnotatedMethods(eventListener.getClass(), EventFilter.class,
                    Object[].class)) {
                return method;
            }
        }
        return null;
    }
}