package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.List;

public class EventType {
    volatile protected List<EventListenerContainer> eventListenerContainer = new ArrayList<EventListenerContainer>();
    volatile protected EventManager eventManager = null;
    volatile protected String name = "";

    public EventType(EventManager eventManager, String name) {
        this.eventManager = eventManager;
        this.name = name;
        eventManager.events.add(this);
    }

    public EventType(EventManager eventManager) {
        this.eventManager = eventManager;
        eventManager.events.add(this);
    }

    public String getName() {
        return name;
    }

    /**
     * Registers an Event Listener to call the onEvent Method.
     * 
     * @param Event
     *            Listener: Event Listener to be registered.
     */
    public final void registerEventListener(Object eventListener) {
        registerEventListener(eventListener, 0);
    }

    /**
     * Registers an Event Listener to call the onEvent Method. You can set a
     * priority, to specify, in which order the events have to be called. The
     * higher the priority value is, the earlier the Event Listener will be
     * called. You can also activate the forceCall parameter, to receive Events,
     * which doesn't belong to that Event Listener.
     * 
     * @param Event
     *            Listener: Event Listener to be registered.
     * @param int: priority. Higher = more important, lower = less.
     * @param boolean: Activate forceCall to receive all Events of this type.
     */
    public final void registerEventListener(Object eventListener, int priority, Object... filter) {
        EventListenerContainer newListener = new EventListenerContainer(eventListener, priority, filter);
        for (int count = 0; count < eventListenerContainer.size(); count++) {
            EventListenerContainer currEventListenerContainer = eventListenerContainer.get(count);
            if (currEventListenerContainer.priority < priority) {
                eventListenerContainer.add(count, newListener);
                return;
            }
        }
        eventListenerContainer.add(newListener);
    }

    /**
     * Calls this event and asks all registered Event Listeners for an Object.
     * 
     * @param Object
     *            : You can send any valid objects to the registered classes.
     * @return List < Object >: Every called class will return an object. They
     *         are collected in this list.
     */
    public final Event callSync(Object... objects) {
        Event event = new Event(this, objects);
        event.sheduleEvent();
        return event;
    }

    /**
     * Calls this event and asks all registered Event Listeners for an Object in
     * the background.
     * 
     * @param Object
     *            : You can send any valid objects to the registered classes.
     * @return EventStream: Every called class will return an object. They are
     *         collected in this list. If the data is collected, which you can
     *         enshure by using EventStream.isDone(), you can get your Object
     *         list with EventStream.getReturn()
     * @throws AsyncEventQueueOverflowException
     */
    public final Event callAsync(Object... objects) throws AsyncEventQueueOverflowException {
        Event event = new Event(this, objects);
        eventManager.asyncEventQueue.add(event);
        return event;
    }

    /**
     * Only used in the Event extending classes. It decides, if a called Event
     * belongs to an Event Listener.
     * 
     * @param filter
     * 
     * @param Event
     *            Listener: Event Listener to process.
     * @param Object
     *            : object given by the call method.
     * @return boolean: true, if the Event Listener should be called.
     */
    protected boolean applyFilter(Object eventlistener, Object[] filter, Object[] in) {
        return true;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        return false;
    }
}