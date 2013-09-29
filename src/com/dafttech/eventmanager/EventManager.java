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
    volatile protected AsyncEventQueue asyncEventQueue = new AsyncEventQueue();

    public EventManager() {
    }

    /**
     * Used to get the asyncEventQueue of the EventManager
     * 
     * @return AsyncEventQueue
     */
    public final AsyncEventQueue getAsyncEventQueue() {
        return asyncEventQueue;
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
                for (String requestedEvent : annotation.events()) {
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

    protected static final List<Method> getAnnotatedMethods(Class<?> targetClass, Class<? extends Annotation> annotation, Class<?> reqType,
            Class<?>... reqArgs) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : targetClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if ((reqType == null || method.getReturnType() == reqType) && Arrays.equals(method.getParameterTypes(), reqArgs)) {
                    method.setAccessible(true);
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

    protected static final List<Field> getAnnotatedFields(Class<?> targetClass, Class<? extends Annotation> annotation, Class<?> reqType) {
        List<Field> fields = new ArrayList<Field>();
        if (reqType == void.class) return fields;
        for (Field field : targetClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                if (reqType == null || field.getType() == reqType) {
                    field.setAccessible(true);
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
}
