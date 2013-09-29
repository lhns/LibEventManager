package com.dafttech.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Event {
    volatile private EventType eventType = null;
    volatile private Object[] in = null;
    volatile private List<Object> out = new ArrayList<Object>();
    volatile private boolean done = false;
    volatile private boolean cancelled = false;

    protected Event(EventType event, Object[] in) {
        this.eventType = event;
        this.in = in;
    }

    protected void sheduleEvent() {
        eventType.onEvent(this);
        if (cancelled) return;
        if (eventType.eventListenerContainer.size() > 0) {
            EventListenerContainer eventListenerContainer = null;
            for (Iterator<EventListenerContainer> i = eventType.eventListenerContainer.iterator(); i.hasNext();) {
                eventListenerContainer = i.next();
                if (isFiltered(eventListenerContainer.eventListener, eventListenerContainer.getFilter())) {
                    try {
                        eventListenerContainer.method.invoke(eventListenerContainer.isStatic ? null : eventListenerContainer.eventListener, this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (cancelled) return;
                }
            }
        }
        done = true;
    }

    private final boolean isFiltered(Object eventListener, Object[] eventFilter) {
        if (eventFilter.length == 0) return true;
        try {
            return eventType.applyFilter(this, eventListener, eventFilter);
        } catch (ArrayIndexOutOfBoundsException e) {
        } catch (ClassCastException e) {
        }
        return false;
    }

    /**
     * Cancel this EventStream to stop the process of calling all the other
     * EventListeners.
     */
    public void cancel() {
        if (done) return;
        cancelled = true;
    }

    /**
     * Add objects to the output list.
     * 
     * @param obj
     *            Object - object to add to the output list.
     */
    public void addOutput(Object obj) {
        out.add(obj);
    }

    /**
     * Check if the event is cancelled
     * 
     * @return boolean - true, if the event was cancelled.
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Check, if all the data of an Async Event is collected.
     * 
     * @return boolean - true, if the event is done.
     */
    public boolean isDone() {
        if (cancelled) return true;
        return done;
    }

    /**
     * Retrieve all objects given, when the event was called
     * 
     * @return Object[] - the objects
     */
    public Object[] getInput() {
        return in;
    }

    /**
     * Retrieve a specific object given, when the event was called
     * 
     * @param num
     *            int - number of the object to request
     * @return Object - the requested object, or null if the number was out of
     *         range
     */
    public Object getInput(int num) {
        if (num < 0 || num >= in.length) return null;
        return in[num];
    }

    /**
     * Retrieve a specific object given, when the event was called and cast it
     * to the given class
     * 
     * @param num
     *            int - number of the object to request
     * @param calst
     *            Class<T> the class to cast to
     * @return T - the requested object casted to T, or null if the number was
     *         out of range
     */
    @SuppressWarnings("unchecked")
    public <T> T getInput(int num, Class<T> cast) {
        if (num < 0 || num >= in.length || !cast.isInstance(in[num])) return null;
        return (T) in[num];
    }

    /**
     * Use this to get all the objects out of the output list.
     * 
     * @return List<Object> - output list, or null if the event is not done.
     */
    public List<Object> getOutput() {
        if (isDone()) return out;
        return null;
    }

    /**
     * Use this to get all the objects out of the output list, but sort out all
     * null values.
     * 
     * @return List<Object> - output list without null values, or null if the
     *         event is not done.
     */
    public List<Object> getCleanOutput() {
        if (isDone()) {
            List<Object> cleanOut = new ArrayList<Object>(out);
            cleanOut.removeAll(Collections.singleton(null));
            return cleanOut;
        }
        return null;
    }

    /**
     * Check, if this Event is a specific EventType.
     * 
     * @param eventType
     *            EventType - EventType to check for.
     * @return boolean - if this event is of that EventType.
     */
    public boolean isEventType(EventType eventType) {
        return this.eventType.equals(eventType);
    }
}
