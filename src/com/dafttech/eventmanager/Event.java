package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Event {
    volatile private EventManager eventManager = null;
    volatile private EventType type = null;
    volatile private List<Object> in = new ArrayList<Object>();
    volatile private List<ListenerContainer> listenerContainers = new LinkedList<ListenerContainer>();
    volatile private List<Object> out = new ArrayList<Object>();
    volatile private boolean filtered = false;
    volatile private boolean done = false;
    volatile private boolean cancelled = false;

    protected Event(EventManager eventManager, EventType type, Object[] in, List<ListenerContainer> listenerContainers) {
        this.eventManager = eventManager;
        this.type = type;
        for (Object obj : in)
            this.in.add(obj);
        this.listenerContainers = listenerContainers == null ? new LinkedList<ListenerContainer>()
                : new LinkedList<ListenerContainer>(listenerContainers);
    }

    protected final void schedule() {
        if (!isDone()) {
            if (!filtered) {
                for (int i = listenerContainers.size() - 1; i >= 0; i--)
                    if (!listenerContainers.get(i).isFiltered(this)) listenerContainers.remove(i);
                filtered = true;
            }
            type.onEvent(this);
            if (cancelled) return;
            for (ListenerContainer listenerContainer : listenerContainers) {
                listenerContainer.invoke(this);
                if (cancelled) return;
            }
            type.onEventPost(this);
            if (cancelled) return;
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

    public List<ListenerContainer> getListenerContainers() {
        return filtered ? listenerContainers : null;
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
     * @return List<Object> - the objects
     */
    public final List<Object> getInput() {
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
        if (index >= 0 && index < in.size()) return in.get(index);
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
        if (index >= 0 && index < in.size() && cast.isInstance(in.get(index))) return (T) in.get(index);
        return null;
    }

    @SuppressWarnings("unchecked")
    public final <T> List<T> getInput(Class<T> cast) {
        List<T> newOut = new ArrayList<T>();
        for (Object obj : in)
            if (cast.isInstance(obj)) newOut.add((T) obj);
        return newOut;
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

    public final Object getOutput(int index) {
        if (index >= 0 && index < out.size()) return out.get(index);
        return null;
    }

    @SuppressWarnings("unchecked")
    public final <T> T getOutput(int index, Class<T> cast) {
        if (index >= 0 && index < out.size() && cast.isInstance(out.get(index))) return (T) out.get(index);
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
