package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dafttech.eventmanager.exception.MissingEventTypeException;
import com.dafttech.eventmanager.exception.WrongEventListenerAnnotationUsageException;

public class EventManager {
    volatile protected List<EventType> events = new ArrayList<EventType>();

    volatile public AsyncEventQueue asyncEventQueue = new AsyncEventQueue();

    public EventManager() {
    }

    /**
     * Used to get the Event instance registerd on this EventManager, if you
     * only have a name as string.
     * 
     * @param String
     *            : name.
     * @return Event: requested event.
     */
    public EventType getEventByName(String name) {
        return new EventTypeGetter(name).getFromList(events);
    }

    public EventType getEventById(int id) {
        return new EventTypeGetter(id).getFromList(events);
    }

    /**
     * Registers an Event Listener to call the onEvent Method.
     * 
     * @param EventType
     *            Listener: Event Listener to be registered.
     */
    public void registerEventListener(Object eventListener, Object... filter) {
        registerPrioritizedEventListener(eventListener, EventType.PRIORITY_STANDARD, filter);
    }

    /**
     * Registers an Event Listener to call the onEvent Method. You can set a
     * priority, to specify, in which order the events have to be called. The
     * higher the priority value is, the earlier the Event Listener will be
     * called. You can also activate the forceCall parameter, to receive Events,
     * which doesn't belong to that Event Listener.
     * 
     * @param EventType
     *            Listener: Event Listener to be registered.
     * @param int: priority. Higher = more important, lower = less.
     * @param boolean: Activate forceCall to receive all Events of this type.
     */
    public void registerPrioritizedEventListener(Object eventListener, int priority, Object... filter) {
        EventType event = null;
        for (Method method : eventListener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                if (method.getParameterTypes().length == 2 && method.getParameterTypes()[0] == Event.class
                        && method.getParameterTypes()[1] == Object[].class) {
                    for (String allowedEvent : method.getAnnotation(EventListener.class).eventNames()) {
                        event = getEventByName(allowedEvent);
                        if (event != null) {
                            event.addEventListenerContainer(new EventListenerContainer(eventListener, method, priority, filter));
                        } else {
                            throw new MissingEventTypeException(allowedEvent);
                        }
                    }
                } else {
                    throw new WrongEventListenerAnnotationUsageException();
                }
            }
        }
    }

    public final void unregisterEventListener(Object eventListener) {
        for (EventType eventType : events) {
            eventType.eventListenerContainer.remove(eventListener);
        }
    }
}