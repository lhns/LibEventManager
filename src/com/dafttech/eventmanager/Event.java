package com.dafttech.eventmanager;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.dafttech.eventmanager.exception.WrongEventListenerTypeException;

public class Event {
    volatile private EventType event = null;
    volatile private Object[] in = null;
    volatile private List<Object> out = new ArrayList<Object>();
    volatile private boolean done = false;
    volatile private boolean cancelled = false;

    protected Event(EventType event, Object[] in) {
        this.event = event;
        this.in = in;
    }

    protected void sheduleEvent() {
        event.onEvent(this, in);
        if (cancelled) return;
        if (event.eventListenerContainer.size() > 0) {
            EventListenerContainer eventListenerContainer = null;
            for (Iterator<EventListenerContainer> i = event.eventListenerContainer.iterator(); i.hasNext();) {
                eventListenerContainer = i.next();
                if (eventListenerContainer.filter.length == 0
                        || event.applyFilter(eventListenerContainer.eventListener, eventListenerContainer.filter, in)) {
                    if (eventListenerContainer.method != null) {
                        try {
                            eventListenerContainer.method.invoke(eventListenerContainer.eventListener, this);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } else if (eventListenerContainer.eventListener instanceof IEventListener) {
                        ((IEventListener) eventListenerContainer.eventListener).onEvent(this);
                    } else {
                        throw new WrongEventListenerTypeException();
                    }
                    if (cancelled) return;
                }
            }
        }
        done = true;
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

    public Object[] getInput() {
        return in;
    }

    public Object getInput(int num) {
        if (num < 0 || num >= in.length) return null;
        return in[num];
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
        if (event.equals(event)) return true;
        return false;
    }
}
