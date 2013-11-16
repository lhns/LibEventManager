package com.dafttech.eventmanager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class EventListenerContainer {
    volatile protected boolean isStatic;
    volatile protected Object eventListener;
    volatile protected Method method;
    volatile protected int priority;

    volatile private Object filter;

    protected EventListenerContainer(boolean isStatic, Object eventListener, Method method, EventListener annotation) {
        this.isStatic = isStatic;
        this.eventListener = eventListener;
        this.method = method;
        this.priority = annotation.priority();
        this.filter = getFilterContainer(isStatic, isStatic ? (Class<?>) this.eventListener : this.eventListener.getClass(), annotation.filter());
    }

    protected Object[] getFilter() {
        Object filterObj = null;
        try {
            if (filter instanceof Field) filterObj = ((Field) filter).get(isStatic ? null : eventListener);
            if (filter instanceof Method) filterObj = ((Method) filter).invoke(isStatic ? null : eventListener);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (filterObj == null) return new Object[0];
        if (filterObj instanceof Object[]) return (Object[]) filterObj;
        return new Object[] { filterObj };
    }

    @Override
    public boolean equals(Object paramObject) {
        if (paramObject instanceof EventListenerContainer) {
            return paramObject == this;
        } else {
            return paramObject == eventListener;
        }
    }

    private static final Object getFilterContainer(boolean isStatic, Class<?> filterClass, String filterName) {
        if (!filterName.equals("")) {
            if (filterName.contains(".")) {
                Class<?> remoteFilterClass = null;
                try {
                    remoteFilterClass = Class.forName(filterName.substring(0, filterName.lastIndexOf('.')));
                } catch (ClassNotFoundException e1) {
                    String packageName = filterClass.getPackage().getName();
                    while (filterName.contains("..") && packageName.contains(".")) {
                        filterName = filterName.replaceFirst("..", "");
                        packageName = packageName.substring(0, packageName.lastIndexOf('.'));
                    }
                    filterName = packageName + '.' + filterName;
                    try {
                        remoteFilterClass = Class.forName(filterName.substring(0, filterName.lastIndexOf('.')));
                    } catch (ClassNotFoundException e2) {
                        e1.printStackTrace();
                        e2.printStackTrace();
                    }
                }
                filterName = filterName.substring(filterName.lastIndexOf('.') + 1);

                if (remoteFilterClass != null) {
                    filterClass = remoteFilterClass;
                    isStatic = true;
                }

            }
            for (Field field : EventManager.getAnnotatedFields(filterClass, EventFilter.class, true, null)) {
                if ((!isStatic || Modifier.isStatic(field.getModifiers())) && field.getAnnotation(EventFilter.class).value().equals(filterName))
                    return field;
            }
            for (Method method : EventManager.getAnnotatedMethods(filterClass, EventFilter.class, true, null)) {
                if ((!isStatic || Modifier.isStatic(method.getModifiers())) && method.getAnnotation(EventFilter.class).value().equals(filterName))
                    return method;
            }
        }
        return null;
    }
}