package com.dafttech.eventmanager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EventType {
    volatile protected String name = "";
    volatile protected EventManager[] managerWhitelist;

    protected static Map<String, EventType> types = new HashMap<String, EventType>();
    public static final int PRIORITY_STANDARD = 0;

    public EventType(String name, EventManager... managerWhitelist) {
        this.name = name;
        types.put(name, this);
        this.managerWhitelist = managerWhitelist;
    }

    protected EventType() {
    }

    public final String getName() {
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
    protected boolean applyFilter(Event event, Object[] filter, ListenerContainer listenerContainer) {
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
        return managerWhitelist.length == 0 || Arrays.asList(managerWhitelist).contains(eventManager);
    }

    @Override
    public final boolean equals(Object object) {
        if (object instanceof EventType) {
            if (object == this) return true;
        } else if (object instanceof String) {
            if (((String) object).equals(name)) return true;
        }
        return false;
    }

    public static final EventType getByName(String name) {
        return types.get(name);
    }
}