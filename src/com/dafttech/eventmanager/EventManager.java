package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.dafttech.eventmanager.exception.AsyncEventQueueOverflowException;
import com.dafttech.eventmanager.exception.WrongEventListenerAnnotationUsageException;
import com.dafttech.eventmanager.exception.WrongEventManagerModeException;

public class EventManager {
    volatile protected List<EventType> events = new ArrayList<EventType>();
    volatile protected EventManagerMode mode = EventManagerMode.INTERFACE;

    volatile public AsyncEventQueue asyncEventQueue = new AsyncEventQueue();

    public EventManager() {
    }

    public EventManager(EventManagerMode mode) {
        this.mode = mode;
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
     * Calls an event and asks all registered Event Listeners for an Object.
     * 
     * @param String
     *            : Event name to call.
     * @param Object
     *            : You can send any valid objects to the registered classes.
     * @return List < Object >: Every called class will return an object. They
     *         are collected in this list.
     */
    public Event callSyncEvent(String eventName, Object... objects) {
        EventType event = getEventByName(eventName);
        if (event == null) return null;
        return event.callSync(objects);
    }

    /**
     * Calls this event and asks all registered Event Listeners for an Object in
     * the background.
     * 
     * @param String
     *            : Event name to call.
     * @param Object
     *            : You can send any valid objects to the registered classes.
     * @return EventStream: Every called class will return an object. They are
     *         collected in this list. If the data is collected, which you can
     *         enshure by using EventStream.isDone(), you can get your Object
     *         list with EventStream.getReturn()
     * @throws AsyncEventQueueOverflowException
     */
    public Event callAsyncEvent(String eventName, Object... objects) throws AsyncEventQueueOverflowException {
        EventType event = getEventByName(eventName);
        if (event == null) return null;
        return event.callAsync(objects);
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
        if (mode == EventManagerMode.INTERFACE) throw new WrongEventManagerModeException();
        EventType event = null;
        for (Method method : eventListener.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventListener.class)) {
                if (method.getParameterTypes().length == 2 && method.getParameterTypes()[0] == Event.class
                        && method.getParameterTypes()[1] == Object[].class) {
                    for (int allowedEvent : method.getAnnotation(EventListener.class).eventTypeId()) {
                        event = getEventById(allowedEvent);
                        if (event != null) event.addEventListenerContainer(new EventListenerContainer(method, eventListener, priority, filter));
                    }
                } else {
                    throw new WrongEventListenerAnnotationUsageException();
                }
            }
        }
    }

    public final void unregisterEventListener(String eventName, Object eventListener) {
        EventType event = getEventByName(eventName);
        if (event == null) return;
        event.unregisterEventListener(eventListener);
    }
}