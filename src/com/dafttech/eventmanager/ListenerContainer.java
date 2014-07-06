package com.dafttech.eventmanager;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.dafttech.util.ReflectionUtil;

public class ListenerContainer extends AnnotatedElementContainer<AnnotatedElement> {
    volatile private AnnotatedElementContainer<AnnotatedElement>[] filters;
    volatile private int priority;

    protected ListenerContainer(AnnotatedElement target, Object access, String[] filters, int priority,
            Map<String, String> filterShortcuts) {
        super(target, access);
        this.filters = getFilterContainers(filters, filterShortcuts);
        this.priority = priority;
    }

    @SuppressWarnings("unchecked")
    private final AnnotatedElementContainer<AnnotatedElement>[] getFilterContainers(String[] filterNames,
            Map<String, String> filterShortcuts) {
        Set<AnnotatedElementContainer<AnnotatedElement>> filterList = new HashSet<AnnotatedElementContainer<AnnotatedElement>>();
        boolean mustBeStatic;
        Class<?> filterClass;
        for (String filterName : filterNames) {
            if (filterName.equals("")) continue;

            mustBeStatic = isStatic;
            filterClass = targetClass;

            if (filterShortcuts.containsKey(filterName)) filterName = filterShortcuts.get(filterName);
            filterName = resolveClassPath(filterName, targetClass.getName());

            if (filterName.contains(".")) {
                try {
                    filterClass = Class.forName(filterName.substring(0, filterName.lastIndexOf('.')));
                    filterName = filterName.substring(filterName.lastIndexOf('.') + 1);
                    mustBeStatic = true;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (filterClass.isAnnotationPresent(EventListener.Filter.class)
                    && filterClass.getAnnotation(EventListener.Filter.class).value().equals(filterName))
                filterList.add(new AnnotatedElementContainer<AnnotatedElement>(filterClass, targetClass, targetInstance));
            for (Field field : ReflectionUtil.getAnnotatedFields(filterClass, EventListener.Filter.class, null)) {
                if ((!mustBeStatic || Modifier.isStatic(field.getModifiers()))
                        && field.getAnnotation(EventListener.Filter.class).value().equals(filterName))
                    filterList.add(new AnnotatedElementContainer<AnnotatedElement>(field, targetClass, targetInstance));
            }
            for (Method method : ReflectionUtil.getAnnotatedMethods(filterClass, EventListener.Filter.class, null,
                    (Class<?>[]) null)) {
                if ((!mustBeStatic || Modifier.isStatic(method.getModifiers()))
                        && method.getAnnotation(EventListener.Filter.class).value().equals(filterName))
                    filterList.add(new AnnotatedElementContainer<AnnotatedElement>(method, targetClass, targetInstance));
            }
            for (Constructor<?> constructor : ReflectionUtil.getAnnotatedConstructors(filterClass, EventListener.Filter.class,
                    (Class<?>[]) null)) {
                if (constructor.getAnnotation(EventListener.Filter.class).value().equals(filterName))
                    filterList.add(new AnnotatedElementContainer<AnnotatedElement>(constructor, targetClass, targetInstance));
            }
        }
        return filterList.toArray(new AnnotatedElementContainer[filterList.size()]);
    }

    private static final String resolveClassPath(String classPath, String relativeClassPath) {
        if (!classPath.contains(".")) return classPath;

        if (classPath.startsWith(".")) classPath = relativeClassPath + classPath;

        int backIndex = classPath.indexOf("..");
        while (backIndex > -1) {
            while (classPath.length() > backIndex + 2 && classPath.charAt(backIndex + 2) == '.')
                backIndex++;
            classPath = classPath.substring(0, classPath.substring(0, backIndex).lastIndexOf(".") + 1)
                    + classPath.substring(backIndex + 2);
            backIndex = classPath.indexOf("..");
        }
        return classPath;
    }

    protected final void invoke(Event event) {
        Object[] args = Arrays.copyOf(nullArgs, nullArgs.length);
        for (int i = 0; i < args.length; i++)
            if (argTypes[i] == Event.class) args[i] = event;
        try {
            if (isMethod()) {
                ((Method) target).invoke(targetInstance, args);
            } else if (isConstructor()) {
                ((Constructor<?>) target).newInstance(args);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("at " + targetClass.getName() + " at method " + target.toString());
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.out.println("at " + targetClass.getName() + " at constructor " + target.toString());
        }
    }

    protected final boolean isFiltered(Event event) {
        if (filters.length == 0) return true;
        try {
            return event.getEventType().isFiltered(event, getFilters(), this);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (ClassCastException e) {
        } catch (NullPointerException e) {
        }
        return false;
    }

    private final Object[] getFilters() {
        Object[] returnObjects = new Object[filters.length];
        AnnotatedElementContainer<AnnotatedElement> filter;
        for (int i = 0; i < filters.length; i++) {
            filter = filters[i];
            if (filter == null) continue;
            try {
                if (filter.isClass()) {
                    returnObjects[i] = filter.target;
                } else if (filter.isField()) {
                    returnObjects[i] = ((Field) filter.target).get(filter.targetInstance);
                } else if (filter.isMethod()) {
                    returnObjects[i] = ((Method) filter.target).invoke(filter.targetInstance, filter.nullArgs);
                } else if (filter.isConstructor()) {
                    returnObjects[i] = ((Constructor<?>) filter.target).newInstance(filter.nullArgs);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
        return returnObjects;
    }

    @Override
    public final boolean equals(Object obj) {
        if (obj instanceof ListenerContainer) {
            ListenerContainer listenerContainer = (ListenerContainer) obj;
            return (listenerContainer.target == target || listenerContainer.target.equals(target))
                    && (listenerContainer.targetClass == targetClass || listenerContainer.targetClass.equals(targetClass))
                    && (listenerContainer.targetInstance == targetInstance || listenerContainer.targetInstance
                            .equals(targetInstance));
        }
        return false;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public Object getEventListener() {
        return targetInstance;
    }

    public AnnotatedElement getTarget() {
        return target;
    }

    public int getPriority() {
        return priority;
    }
}