package com.dafttech.eventmanager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class EventManager {
    volatile protected List<EventType> events = new ArrayList<EventType>();

    public EventManager() {
    }

    /**
     * Used to get the instance of the EventType registered with that name.
     * 
     * @param name
     *            String
     * @return EventType
     */
    public final EventType getEventByName(String name) {
        return new EventTypeGetter(name).getFromList(events);
    }

    /**
     * Used to get the instance of the EventType registered with that id.
     * 
     * @param id
     *            int
     * @return EventType
     */
    public final EventType getEventById(int id) {
        return new EventTypeGetter(id).getFromList(events);
    }

    /**
     * Used to register an EventListener created with annotations to call the
     * annotated methods.
     * 
     * @param eventListener
     *            Object - Instance of the listening class
     * @param filter
     *            Object... - Sets the filter that is customizable in EventType
     *            subclasses
     */
    public final void registerEventListener(Object eventListener) {
        registerAnnotatedMethods(eventListener, null);
    }

    protected final void registerAnnotatedMethods(Object eventListener, EventType eventType) {
        boolean eventListenerStatic = eventListener.getClass() == Class.class;
        Class<?> eventListenerClass = eventListenerStatic ? (Class<?>) eventListener : eventListener.getClass();
        EventListener annotation = null;
        boolean isStatic = false;
        EventType event = null;
        for (Method method : getAnnotatedMethods(eventListenerClass, EventListener.class, void.class, Event.class)) {
            annotation = method.getAnnotation(EventListener.class);
            isStatic = Modifier.isStatic(method.getModifiers());
            if (!eventListenerStatic || isStatic) {
                for (String requestedEvent : annotation.value()) {
                    if (eventType == null || eventType.equals(requestedEvent)) {
                        event = getEventByName(requestedEvent);
                        if (event != null) {
                            event.addEventListenerContainer(new EventListenerContainer(isStatic, isStatic ? eventListenerClass : eventListener,
                                    method, annotation));
                        } else {
                            throw new NoSuchElementException(requestedEvent);
                        }
                    }
                }
            }
        }
    }

    /**
     * Used to unregister an EventListener in all events.
     * 
     * @param eventListener
     *            Object - Instance of the listening class
     */
    public final void unregisterEventListener(Object eventListener) {
        for (EventType eventType : events) {
            eventType.unregisterEventListener(eventListener);
        }
    }

    public static final List<Method> getAnnotatedMethods(Class<?> targetClass, Class<? extends Annotation> annotation, Class<?> reqType,
            Class<?>... reqArgs) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : getAllDeclaredMethods(targetClass)) {
            if (method.isAnnotationPresent(annotation)) {
                if ((reqType == null || method.getReturnType() == reqType) && Arrays.equals(method.getParameterTypes(), reqArgs)) {
                    methods.add(method);
                } else {
                    String errorMessage = "\nat " + targetClass.getName() + " at Annotation " + annotation.getName() + ":";
                    errorMessage += "\nexpected: " + reqType.getName() + " with " + (reqArgs.length == 0 ? "no args" : "args:");
                    for (Class<?> arg : reqArgs)
                        errorMessage += ", " + arg.getName();
                    errorMessage += "\nand got:  " + method.getReturnType() + " with "
                            + (method.getParameterTypes().length == 0 ? "no args" : "args:");
                    for (Class<?> arg : method.getParameterTypes())
                        errorMessage += ", " + arg.getName();
                    errorMessage += ".";
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }
        return methods;
    }

    public static final List<Field> getAnnotatedFields(Class<?> targetClass, Class<? extends Annotation> annotation, Class<?> reqType) {
        List<Field> fields = new ArrayList<Field>();
        if (reqType == void.class) return fields;
        for (Field field : getAllDeclaredFields(targetClass)) {
            if (field.isAnnotationPresent(annotation)) {
                if (reqType == null || field.getType() == reqType) {
                    fields.add(field);
                } else {
                    String errorMessage = "\nat " + targetClass.getName() + " at Annotation " + annotation.getName() + ":";
                    errorMessage += "\nexpected: " + reqType.getName();
                    errorMessage += "\nand got:  " + field.getType();
                    errorMessage += ".";
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        }
        return fields;
    }

    public static List<Method> getAllDeclaredMethods(Class<?> targetClass) {
        return getAllDeclaredMethods(targetClass, null);
    }

    public static List<Field> getAllDeclaredFields(Class<?> targetClass) {
        return getAllDeclaredFields(targetClass, null);
    }

    private static List<Method> getAllDeclaredMethods(Class<?> targetClass, List<Method> methods) {
        if (methods == null) methods = new ArrayList<Method>();
        for (Method method : targetClass.getDeclaredMethods())
            if (!methods.contains(method)) {
                method.setAccessible(true);
                methods.add(method);
            }
        if (targetClass.getSuperclass() != null) getAllDeclaredMethods(targetClass.getSuperclass(), methods);
        return methods;
    }

    private static List<Field> getAllDeclaredFields(Class<?> targetClass, List<Field> fields) {
        if (fields == null) fields = new ArrayList<Field>();
        for (Field field : targetClass.getDeclaredFields())
            if (!fields.contains(field)) {
                field.setAccessible(true);
                fields.add(field);
            }
        if (targetClass.getSuperclass() != null) getAllDeclaredFields(targetClass.getSuperclass(), fields);
        return fields;
    }
}
