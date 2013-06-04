package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EventManager {
    volatile protected List<EventType> events = new ArrayList<EventType>();
    volatile public AsyncEventQueue asyncEventQueue = new AsyncEventQueue();

    public static enum EventListenerMode {
        onEvent, Annotation
    };

    volatile public EventListenerMode eventListenerMode = EventListenerMode.onEvent;

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
        EventType event = null;
        for (Iterator<EventType> i = events.iterator(); i.hasNext();) {
            event = i.next();
            if (event.name.equals(name)) return event;
        }
        return null;
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
    public void registerEventListener(String eventName, Object eventListener) {
        EventType event = getEventByName(eventName);
        if (event == null) return;
        event.registerEventListener(eventListener, 0);
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
    public void registerEventListener(String eventName, Object eventListener, int priority, Object... filter) {
        EventType event = getEventByName(eventName);
        if (event == null) return;
        event.registerEventListener(eventListener, priority, filter);
    }
}