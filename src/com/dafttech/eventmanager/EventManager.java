package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.dafttech.filterlist.Blacklist;
import com.dafttech.filterlist.Filterlist;
import com.dafttech.reflect.Reflector;

public class EventManager {
    volatile protected Map<EventType, List<ListenerContainer>> registeredListeners = new HashMap<EventType, List<ListenerContainer>>();

    public EventManager() {
    }

    /**
     * Used to register an EventListener created with annotations to call the
     * annotated methods.
     * 
     * @param eventListener
     *            Object - Instance of the listening class
     * @param filterlist
     *            Filterlist<EventType> - Sets a filterlist of EventTypes that
     *            are included/excluded
     */
    public final void registerEventListener(Object eventListener, Filterlist<EventType> filterlist) {
        if (filterlist == null || !filterlist.isValid()) return;
        boolean isStatic = eventListener.getClass() == Class.class, isListenerStatic;
        Class<?> eventListenerClass = isStatic ? (Class<?>) eventListener : eventListener.getClass();
        EventListener annotation = null;
        EventType type = null;
        Reflector reflector = new Reflector(eventListenerClass).showExceptions(true);
        for (Method method : reflector.getAnnotatedMethods(EventListener.class, null, (Class<?>) null)) {
            annotation = method.getAnnotation(EventListener.class);
            isListenerStatic = Modifier.isStatic(method.getModifiers());
            if (!isStatic || isListenerStatic) {
                for (String requestedEvent : annotation.value()) {
                    type = EventType.types.get(requestedEvent);
                    if (type != null) {
                        if (type.isWhitelisted(this) && filterlist.isFiltered(type)) {
                            addEventListenerContainer(type, new ListenerContainer(isListenerStatic,
                                    isListenerStatic ? eventListenerClass : eventListener, method, annotation));
                        }
                    } else {
                        throw new NoSuchElementException(requestedEvent);
                    }
                }
            }
        }
    }

    public final void registerEventListener(Object eventListener) {
        registerEventListener(eventListener, new Blacklist<EventType>());
    }

    public final void tryRegisterEventListener(String staticEventListener, Filterlist<EventType> filterlist) {
        try {
            registerEventListener(Class.forName(staticEventListener), filterlist);
        } catch (ClassNotFoundException e) {
        }
    }

    public final void tryRegisterEventListener(String staticEventListener) {
        tryRegisterEventListener(staticEventListener, new Blacklist<EventType>());
    }

    public final void unregisterEventListener(Object eventListener, Filterlist<EventType> filterlist) {
        if (filterlist == null || !filterlist.isValid()) return;
        List<ListenerContainer> listenerContainers;
        for (EventType type : registeredListeners.keySet()) {
            if (filterlist.isFiltered(type)) {
                listenerContainers = registeredListeners.get(type);
                if (listenerContainers != null && listenerContainers.size() > 0) {
                    for (int i = listenerContainers.size() - 1; i >= 0; i--)
                        if (listenerContainers.get(i).equals(eventListener)) listenerContainers.remove(i);
                }
            }
        }
    }

    public final void unregisterEventListener(Object eventListener) {
        unregisterEventListener(eventListener, new Blacklist<EventType>());
    }

    private final void addEventListenerContainer(EventType type, ListenerContainer newListenerContainer) {
        if (!registeredListeners.containsKey(type) || registeredListeners.get(type) == null)
            registeredListeners.put(type, new ArrayList<ListenerContainer>());
        List<ListenerContainer> listenerContainers = registeredListeners.get(type);
        ListenerContainer listenerContainer;
        for (int i = 0; i < listenerContainers.size(); i++) {
            listenerContainer = listenerContainers.get(i);
            if (listenerContainer == newListenerContainer) return;
            if (listenerContainer.getPriority() < newListenerContainer.getPriority()) {
                listenerContainers.add(i, newListenerContainer);
                return;
            }
        }
        listenerContainers.add(newListenerContainer);
    }

    /**
     * Calls this event and asks all registered EventListeners and sends the
     * objects to them.
     * 
     * @param type
     *            EventType - The EventType you want to call.
     * @param objects
     *            Object... - You can send any objects to the registered
     *            classes.
     * @return Event - to manage the called event such as getting the output and
     *         checking if the event was cancelled
     */
    public final Event callSync(EventType type, Object... objects) {
        if (type == null) return null;
        Event event = new Event(this, type, objects, registeredListeners.get(type));
        event.schedule();
        return event;
    }

    /**
     * Calls this event in another thread that has to be started with
     * eventManagerInstance.asyncEventQueue.start(). It asks all registered
     * EventListeners and sends the objects to them.
     * 
     * @param type
     *            EventType - The EventType you want to call.
     * @param objects
     *            Object... - You can send any objects to the registered
     *            classes.
     * @return Event - to manage the called event such as checking if the event
     *         is done, getting the output and checking if the event was
     *         cancelled
     */
    public final Event callAsync(EventType type, Object... objects) {
        if (type == null) return null;
        Event event = new Event(this, type, objects, registeredListeners.get(type));
        new AsyncEventThread(event);
        return event;
    }
}
