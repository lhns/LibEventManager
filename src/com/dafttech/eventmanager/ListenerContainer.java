package com.dafttech.eventmanager;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dafttech.type.Type;
import com.dafttech.type.TypeClass;

public class ListenerContainer extends AccObjContainer<Method> {
    volatile private int priority;

    volatile private AccObjContainer<AccessibleObject>[] filters;

    protected ListenerContainer(Method target, Object access, EventListener annotation) {
        super(target, access);
        priority = annotation.priority();
        filters = getFilterContainers(annotation.filter());
    }

    @SuppressWarnings("unchecked")
    private final AccObjContainer<AccessibleObject>[] getFilterContainers(String[] filterNames) {
        List<AccObjContainer<AccessibleObject>> filterList = new ArrayList<AccObjContainer<AccessibleObject>>();
        boolean mustBeStatic;
        Class<?> filterClass;
        for (String filterName : filterNames) {
            if (filterName.equals("")) continue;
            mustBeStatic = isStatic;
            filterClass = targetClass;
            if (filterName.contains(".")) {
                if (filterName.startsWith(".")) filterName = targetClass.getName() + filterName;
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
                    mustBeStatic = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
            TypeClass reflector = Type.CLASS.<TypeClass> create(filterClass);
            for (Field field : reflector.getAnnotatedFields(EventFilter.class, null)) {
                if ((!mustBeStatic || Modifier.isStatic(field.getModifiers()))
                        && field.getAnnotation(EventFilter.class).value().equals(filterName))
                    filterList.add(new AccObjContainer<AccessibleObject>(field, targetClass, targetInstance));
            }
            for (Method method : reflector.getAnnotatedMethods(EventFilter.class, null, (Class<?>[]) null)) {
                if ((!mustBeStatic || Modifier.isStatic(method.getModifiers()))
                        && method.getAnnotation(EventFilter.class).value().equals(filterName))
                    filterList.add(new AccObjContainer<AccessibleObject>(method, targetClass, targetInstance));
            }
        }
        return filterList.toArray(new AccObjContainer[0]);
    }

    protected final void invoke(Event event) {
        Object[] args = Arrays.copyOf(nullArgs, nullArgs.length);
        for (int i = 0; i < args.length; i++)
            if (argTypes[i] == Event.class) args[i] = event;
        try {
            target.invoke(targetInstance, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("at " + (isStatic ? ((Class<?>) targetInstance).getName() : targetInstance.getClass().getName())
                    + " at method " + target.getName());
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
        AccObjContainer<AccessibleObject> filter;
        Object retObj;
        for (int i = 0; i < filters.length; i++) {
            filter = filters[i];
            retObj = null;
            if (filter != null) {
                try {
                    if (filter.isField()) retObj = ((Field) filter.target).get(filter.targetInstance);
                    if (filter.isMethod()) retObj = ((Method) filter.target).invoke(filter.targetInstance, filter.nullArgs);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (retObj == null) {
                filterArray[i] = new Object[0];
            } else if (retObj instanceof Object[]) {
                filterArray[i] = (Object[]) retObj;
            } else {
                filterArray[i] = new Object[] { retObj };
            }
        }
        return filterArray;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof ListenerContainer) {
            return ((ListenerContainer) obj).target.equals(target) && ((ListenerContainer) obj).isStatic == isStatic;
        } else {
            return obj == targetInstance || obj == targetClass || obj.equals(targetInstance) || obj.equals(targetClass);
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Object getEventListener() {
        return targetInstance;
    }

    public Method getMethod() {
        return target;
    }

    public int getPriority() {
        return priority;
    }
}