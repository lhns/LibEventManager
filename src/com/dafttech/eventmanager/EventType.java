package com.dafttech.eventmanager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventType {
    volatile private String name = "";
    volatile private EventManager[] eventManagerWhitelist;

    protected static Map<String, EventType> types = new HashMap<String, EventType>();

    public static final double PRIORITY_LOW = -1;
    public static final double PRIORITY_NORMAL = 0;
    public static final double PRIORITY_HIGH = 1;

    public EventType(String name, EventManager... eventManagerWhitelist) {
        this.name = name;
        types.put(name, this);
        this.eventManagerWhitelist = eventManagerWhitelist;
    }

    protected EventType() {
    }

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Used to be overridden in subclasses (use anonymous classes), to filter
     * out the EventListeners
     * 
     * @param event
     *            Event - the called event
     * @param filter
     *            Object[] - Is the given filter on registering an EventListener
     * @param listenerContainer
     *            ListenerContainer - Is the container of the EventListener
     * 
     * @return boolean: true, if the EventListener should be called.
     */
    protected boolean isFiltered(Event event, Object[] filter, ListenerContainer listenerContainer) {
        return true;
    }

    /**
     * Used to be overridden in subclasses (use anonymous classes), to catch the
     * events before all the EventListeners
     * 
     * @param event
     *            Event - Is the called event (can be cancelled)
     */
    protected void onEvent(Event event) {
    }

    protected void onEventPost(Event event) {
    }

    protected final boolean isWhitelisted(EventManager eventManager) {
        return eventManagerWhitelist.length == 0 || Arrays.asList(eventManagerWhitelist).contains(eventManager);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof EventType) {
            if (object == this) return true;
        } else if (object instanceof String) {
            if (name.equals(object)) return true;
        }
        return false;
    }

    public static final EventType getByName(String name) {
        return types.get(name);
    }
}