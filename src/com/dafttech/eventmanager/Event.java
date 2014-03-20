package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.List;

public class Event {
    volatile private EventManager eventManager = null;
    volatile private EventType type = null;
    volatile private Object[] in = null;
    volatile private List<EventListenerContainer> eventListenerContainers = new ArrayList<EventListenerContainer>();
    volatile private List<Object> out = new ArrayList<Object>();
    volatile private boolean done = false;
    volatile private boolean cancelled = false;

    protected Event(EventManager eventManager, EventType type, Object[] in) {
        this.eventManager = eventManager;
        this.type = type;
        this.in = in;
    }

    protected final void schedule(List<EventListenerContainer> listeners) {
        if (!done) {
            eventListenerContainers.clear();
            if (listeners != null) for (EventListenerContainer listener : listeners)
                if (listener.isFiltered(this)) eventListenerContainers.add(listener);
            type.onEvent(this);
            if (cancelled) return;
            for (EventListenerContainer listener : eventListenerContainers) {
                listener.invoke(this);
                if (cancelled) return;
            }
            done = true;
        }
    }

    /**
     * Returns the EventManager, that handles this Event
     * 
     * @return EventManager - the EventManager which handles this Event.
     */
    public final EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Returns the EventType of this Event
     * 
     * @return EventType - the EventType this event is of.
     */
    public final EventType getEventType() {
        return type;
    }

    /**
     * Check if the Event is of the given EventType
     * 
     * @param eventType
     *            EventType - EventType to check for.
     * @return boolean - if the EventType was equal to the given one.
     */
    public final boolean isEventType(EventType eventType) {
        return type.equals(eventType);
    }

    /**
     * Cancel this EventStream to stop the process of calling all the other
     * EventListeners.
     */
    public final void cancel() {
        if (done) return;
        cancelled = true;
    }

    public List<EventListenerContainer> getEventListenerContainers() {
        return eventListenerContainers;
    }

    /**
     * Add objects to the output list.
     * 
     * @param obj
     *            Object - object to add to the output list.
     */
    public final void addOutput(Object obj) {
        out.add(obj);
    }

    /**
     * Check if the event is cancelled
     * 
     * @return boolean - true, if the event was cancelled.
     */
    public final boolean isCancelled() {
        return cancelled;
    }

    /**
     * Check, if all the data of an Async Event is collected.
     * 
     * @return boolean - true, if the event is done.
     */
    public final boolean isDone() {
        return done || cancelled;
    }

    /**
     * Retrieve all objects given, when the event was called
     * 
     * @return Object[] - the objects
     */
    public final Object[] getInput() {
        return in;
    }

    /**
     * Retrieve a specific object given, when the event was called
     * 
     * @param index
     *            int - number of the object to request
     * @return Object - the requested object, or null if the number was out of
     *         range
     */
    public final Object getInput(int index) {
        if (index >= 0 && index < in.length) return in[index];
        return null;
    }

    /**
     * Retrieve a specific object given, when the event was called and cast it
     * to the given class
     * 
     * @param index
     *            int - number of the object to request
     * @param cast
     *            Class<T> the class to cast to
     * @return T - the requested object casted to T, or null if the number was
     *         out of range
     */
    @SuppressWarnings("unchecked")
    public final <T> T getInput(int index, Class<T> cast) {
        if (index >= 0 && index < in.length && cast.isInstance(in[index])) return (T) in[index];
        return null;
    }

    /**
     * Use this to get all the objects out of the output list.
     * 
     * @return List<Object> - output list, or null if the event is not done.
     */
    public final List<Object> getOutput() {
        if (isDone()) return out;
        return null;
    }

    /**
     * Use this to get all the objects out of the output list, but sort out all
     * null values.
     * 
     * @param cast
     *            Class<T> With this argument you can filter outputs of specific
     *            types and get a casted list
     * @return List<Object> - output list without null values, or null if the
     *         event is not done.
     */
    @SuppressWarnings("unchecked")
    public final <T> List<T> getOutput(Class<T> cast) {
        if (isDone()) {
            List<T> newOut = new ArrayList<T>();
            for (Object obj : out)
                if (cast.isInstance(obj)) newOut.add((T) obj);
            return newOut;
        }
        return null;
    }

    @Deprecated
    public final List<Object> getCleanOutput() {
        return getOutput(Object.class);
    }

    @Deprecated
    public final String getType() {
        return type.name;
    }
}
