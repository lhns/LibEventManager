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
        if (cancelled || done) return;
        EventListenerContainer eventListenerContainer = null;
        event.onEvent(this, in);
        for (Iterator<EventListenerContainer> i = event.eventListenerContainer.iterator(); i.hasNext();) {
            eventListenerContainer = i.next();
            if (eventListenerContainer.filter.length == 0
                    || event.applyFilter(eventListenerContainer.eventListener, eventListenerContainer.filter, in)) {
                if (eventListenerContainer.method != null) {
                    try {
                        eventListenerContainer.method.invoke(eventListenerContainer.eventListener, this, in);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else if (eventListenerContainer.eventListener instanceof IEventListener) {
                    ((IEventListener) eventListenerContainer.eventListener).onEvent(this, in);
                } else {
                    throw new WrongEventListenerTypeException();
                }
                if (cancelled) return;
            }
        }
        done = true;
    }

    /**
     * Cancel this EventStream to stop the process of calling all the other
     * registered classes.
     */
    public void cancel() {
        if (done) return;
        cancelled = true;
    }

    public void addOutput(Object obj) {
        out.add(obj);
    }

    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Check, if all the data of an Async Event is collected.
     */
    public boolean isDone() {
        if (cancelled) return true;
        return done;
    }

    /**
     * If the Async Event is done, you can get your Return by using this. After
     * using this, this EventStream will not be available for you anymore.
     */
    public List<Object> getOutput() {
        if (isDone()) return out;
        return null;
    }

    public List<Object> getCleanOutput() {
        if (isDone()) {
            List<Object> cleanOut = new ArrayList<Object>(out);
            cleanOut.removeAll(Collections.singleton(null));
            return cleanOut;
        }
        return null;
    }

    /**
     * Check, which Event this EventStream is.
     */
    public boolean isEventType(EventType eventType) {
        if (event.equals(event)) return true;
        return false;
    }
}
