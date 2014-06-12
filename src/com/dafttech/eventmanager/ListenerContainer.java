package com.dafttech.eventmanager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.dafttech.type.Type;
import com.dafttech.type.TypeClass;

public class ListenerContainer {
    volatile private boolean isStatic;
    volatile private Object eventListener;
    volatile private Method method;
    volatile private int priority;

    volatile private Object[] filters;
    volatile private Class<?>[] argTypes;

    protected ListenerContainer(boolean isStatic, Object eventListener, Method method, EventListener annotation) {
        this.isStatic = isStatic;
        this.eventListener = eventListener;
        this.method = method;
        priority = annotation.priority();
        filters = getFilterContainers(isStatic, isStatic ? (Class<?>) this.eventListener : this.eventListener.getClass(),
                annotation.filter());
        argTypes = method.getParameterTypes();
    }

    protected final void invoke(Event event) {
        try {
            method.invoke(isStatic ? null : eventListener, TypeClass.buildArgumentArray(argTypes, Event.class, event));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("at " + (isStatic ? ((Class<?>) eventListener).getName() : eventListener.getClass().getName())
                    + " at method " + method.getName());
        }
    }

    protected final boolean isFiltered(Event event) {
        if (filters.length == 0) return true;
        Object[][] eventFilters = getFilters();
        if (eventFilters.length == 0) return true;
        for (Object[] eventFilter : eventFilters) {
            if (eventFilter.length == 0) continue;
            try {
                if (event.getEventType().isFiltered(event, eventFilter, this)) return true;
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (ClassCastException e) {
            } catch (NullPointerException e) {
            }
        }
        return false;
    }

    private final Object[][] getFilters() {
        Object[][] filterArray = new Object[filters.length][];
        Object filter, filterObj;
        for (int i = 0; i < filters.length; i++) {
            filter = filters[i];
            filterObj = null;
            if (filter != null) {
                try {
                    if (filter instanceof Field) filterObj = ((Field) filter).get(isStatic ? null : eventListener);
                    if (filter instanceof Method)
                        filterObj = ((Method) filter).invoke(isStatic ? null : eventListener,
                                TypeClass.buildArgumentArray(((Method) filter).getParameterTypes()));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (filterObj == null) {
                filterArray[i] = new Object[0];
            } else if (filterObj instanceof Object[]) {
                filterArray[i] = (Object[]) filterObj;
            } else {
                filterArray[i] = new Object[] { filterObj };
            }
        }
        return filterArray;
    }

    private static final Object[] getFilterContainers(boolean isListenerStatic, Class<?> listenerClass, String[] filterNames) {
        List<Object> filterList = new ArrayList<Object>();
        boolean isStatic;
        Class<?> filterClass;
        for (String filterName : filterNames) {
            if (filterName.equals("")) continue;
            isStatic = isListenerStatic;
            filterClass = listenerClass;
            if (filterName.contains(".")) {
                if (filterName.startsWith(".")) filterName = listenerClass.getName() + filterName;
                if (filterName.contains("..")) {
                    int backIndex;
                    while (filterName.contains("..")) {
                        backIndex = filterName.indexOf("..");
                        while (filterName.length() > backIndex + 2 && filterName.charAt(backIndex + 2) == '.')
                            backIndex++;
                        filterName = filterName.substring(0, filterName.substring(0, backIndex).lastIndexOf(".") + 1)
                                + filterName.substring(backIndex + 2);
                    }
                }
                try {
                    filterClass = Class.forName(filterName.substring(0, filterName.lastIndexOf('.')));
                    filterName = filterName.substring(filterName.lastIndexOf('.') + 1);
                    isStatic = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            TypeClass reflector = Type.CLASS.create(filterClass).showExceptions(true);
            for (Field field : reflector.getAnnotatedFields(EventFilter.class, null)) {
                if ((!isStatic || Modifier.isStatic(field.getModifiers()))
                        && field.getAnnotation(EventFilter.class).value().equals(filterName)) filterList.add(field);
            }
            for (Method method : reflector.getAnnotatedMethods(EventFilter.class, null, (Class<?>) null)) {
                if ((!isStatic || Modifier.isStatic(method.getModifiers()))
                        && method.getAnnotation(EventFilter.class).value().equals(filterName)) filterList.add(method);
            }
        }
        return filterList.toArray();
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof ListenerContainer) {
            return ((ListenerContainer) obj).method.equals(method) && ((ListenerContainer) obj).isStatic == isStatic;
        } else {
            return obj == eventListener || obj.equals(eventListener);
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Object getEventListener() {
        return eventListener;
    }

    public Method getMethod() {
        return method;
    }

    public int getPriority() {
        return priority;
    }
}