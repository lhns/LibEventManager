package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dafttech.eventmanager.exception.MissingEventTypeException;
import com.dafttech.eventmanager.exception.WrongEventListenerAnnotationUsageException;

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
    public final void registerEventListener(Object eventListener, Object... filter) {
        registerPrioritizedEventListener(eventListener, EventType.PRIORITY_STANDARD, filter);
    }

    /**
     * Used to register an EventListener with a specific priority created with
     * annotations to call the annotated methods.
     * 
     * @param eventListener
     *            Object - Instance of the listening class
     * @param priority
     *            int - Higher priority = earlier called
     * @param filter
     *            Object... - Sets the filter that is customizable in EventType
     *            subclasses
     */
    public final void registerPrioritizedEventListener(Object eventListener, int priority, Object... filter) {
        registerAnnotatedMethods(eventListener, priority, filter, null);
    }

    protected final void registerAnnotatedMethods(Object eventListener, int priority, Object[] filter, EventType eventType) {
        EventType event = null;
        for (Method method : eventListener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                if (method.getParameterTypes().length == 1 && method.getParameterTypes()[0] == Event.class) {
                    for (String allowedEvent : method.getAnnotation(EventListener.class).events()) {
                        if (eventType == null || eventType.equals(allowedEvent)) {
                            event = getEventByName(allowedEvent);
                            if (event != null) {
                                event.addEventListenerContainer(new EventListenerContainer(eventListener, method, priority, filter));
                            } else {
                                throw new MissingEventTypeException(allowedEvent);
                            }
                        }
                    }
                } else {
                    throw new WrongEventListenerAnnotationUsageException();
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
            eventType.eventListenerContainer.remove(eventListener);
        }
    }
}
