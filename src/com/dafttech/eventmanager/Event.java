package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
        for (Iterator<EventListenerContainer> i = event.eventListenerContainer.iterator(); i.hasNext();) {
            eventListenerContainer = i.next();
            if (eventListenerContainer.filter.length == 0
                    || event.applyFilter(eventListenerContainer.eventlistener, eventListenerContainer.filter, in)) {
                if (event.eventManager.eventListenerMode == EventManager.EventListenerMode.Annotation) {
                    for (Method method : eventListenerContainer.eventlistener.getClass().getMethods()) {
                        if (method.isAnnotationPresent(EventListener.class)) {
                            String allowedEvents[] = method.getAnnotation(EventListener.class).value();
                            if (Arrays.asList(allowedEvents).contains(event.name)) {
                                try {
                                    out.add(method.invoke(eventListenerContainer.eventlistener, this, in));
                                    if (cancelled) return;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (event.eventManager.eventListenerMode == EventManager.EventListenerMode.onEvent) {
                    if (eventListenerContainer.eventlistener instanceof IEventListener) {
                        out.add(((IEventListener) eventListenerContainer.eventlistener).onEvent(this, in));
                        if (cancelled) return;
                    }
                }
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

    /**
     * Check, if all the data of an Async Event is collected.
     */
    public boolean isDone() {
        if (cancelled) return true;
        return done;
    }

    public boolean isCancelled() {
        return cancelled;
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
