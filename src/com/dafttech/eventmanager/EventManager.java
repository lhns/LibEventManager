package com.dafttech.eventmanager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        EventType event = null;
        EventListener annotation = null;
        for (Method method : getAnnotatedMethods(eventListener.getClass(), EventListener.class, null, Event.class)) {
            annotation = method.getAnnotation(EventListener.class);
            for (String requestedEvent : annotation.events()) {
                if (eventType == null || eventType.equals(requestedEvent)) {
                    event = getEventByName(requestedEvent);
                    if (event != null) {
                        event.addEventListenerContainer(new EventListenerContainer(eventListener, method, annotation));
                    } else {
                        throw new NoSuchElementException(requestedEvent);
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

    protected static final List<Method> getAnnotatedMethods(Class<?> targetClass,
            Class<? extends Annotation> annotation, Class<?> reqType, Class<?>... reqArgs) {
        List<Method> methods = new ArrayList<Method>();
        if (reqType == null) reqType = void.class;
        for (Method method : targetClass.getMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                if (method.getReturnType() == reqType && Arrays.equals(method.getParameterTypes(), reqArgs)) {
                    methods.add(method);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return methods;
    }

    protected static final List<Field> getAnnotatedFields(Class<?> targetClass, Class<? extends Annotation> annotation,
            Class<?> reqType) {
        List<Field> fields = new ArrayList<Field>();
        if (reqType == null) return fields;
        for (Field field : targetClass.getFields()) {
            if (field.isAnnotationPresent(annotation)) {
                if (field.getType() == reqType) {
                    fields.add(field);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        }
        return fields;
    }
}
