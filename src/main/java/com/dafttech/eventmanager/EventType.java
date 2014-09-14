package com.dafttech.eventmanager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.dafttech.hash.HashUtil;
import com.dafttech.storage.tuple.Tuple;

public class EventType {
    private final String name;
    private final EventManager[] eventManagerWhitelist;

    protected static final Map<String, EventType> types = new HashMap<String, EventType>();

    public static final double PRIORITY_LOW = -1;
    public static final double PRIORITY_NORMAL = 0;
    public static final double PRIORITY_HIGH = 1;

    public EventType(String name, EventManager... eventManagerWhitelist) {
        this.name = name;
        this.eventManagerWhitelist = eventManagerWhitelist;

        types.put(name, this);
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
     *            Tuple - Is the given filter on registering an EventListener
     * @param listenerContainer
     *            ListenerContainer - Is the container of the EventListener
     * 
     * @return boolean: true, if the EventListener should be called.
     */
    protected boolean isFiltered(Event event, Tuple filter, ListenerContainer listenerContainer) {
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

    public static final EventType getByName(String name) {
        return types.get(name);
    }

    @Override
    public int hashCode() {
        return HashUtil.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        return HashUtil.equals(this, obj);
    }
}