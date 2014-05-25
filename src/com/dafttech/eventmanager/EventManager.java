package com.dafttech.eventmanager;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.dafttech.reflect.Reflector;

public class EventManager {
    public static final EventType WHITELIST = new EventType();

    volatile protected Map<EventType, List<ListenerContainer>> registeredListeners = new HashMap<EventType, List<ListenerContainer>>();

    public EventManager() {
    }

    /**
     * Used to register an EventListener created with annotations to call the
     * annotated methods.
     * 
     * @param eventListener
     *            Object - Instance of the listening class
     * @param blacklist
     *            EventType... - Sets a blacklist of EventTypes that are then
     *            not registered in the given eventListener class. That can be
     *            converted to a whitelist if you put EventManager.WHITELIST at
     *            the first place.
     */
    public final void registerEventListener(Object eventListener, EventType... blacklist) {
        if (blacklist.length == 1 && blacklist[0] == WHITELIST) return;
        boolean isStatic = eventListener.getClass() == Class.class, isListenerStatic;
        Class<?> eventListenerClass = isStatic ? (Class<?>) eventListener : eventListener.getClass();
        EventListener annotation = null;
        EventType type = null;
        for (Method method : Reflector.getAnnotatedMethods(eventListenerClass, EventListener.class, true, null, (Class<?>) null)) {
            annotation = method.getAnnotation(EventListener.class);
            isListenerStatic = Modifier.isStatic(method.getModifiers());
            if (!isStatic || isListenerStatic) {
                for (String requestedEvent : annotation.value()) {
                    type = EventType.types.get(requestedEvent);
                    if (type != null) {
                        if (type.isWhitelisted(this)
                                && (blacklist.length == 0 || blacklist[0] == WHITELIST && Arrays.asList(blacklist).contains(type) || blacklist[0] != WHITELIST
                                        && !Arrays.asList(blacklist).contains(type))) {
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

    public final void tryRegisterEventListener(String staticEventListener, EventType... blacklist) {
        try {
            registerEventListener(Class.forName(staticEventListener), blacklist);
        } catch (ClassNotFoundException e) {
        }
    }

    public final void unregisterEventListener(Object eventListener, EventType... blacklist) {
        if (blacklist.length == 1 && blacklist[0] == WHITELIST) return;
        List<ListenerContainer> listenerContainers;
        for (EventType type : registeredListeners.keySet()) {
            if (blacklist.length == 0 || blacklist[0] == WHITELIST && Arrays.asList(blacklist).contains(type)
                    || blacklist[0] != WHITELIST && !Arrays.asList(blacklist).contains(type)) {
                listenerContainers = registeredListeners.get(type);
                if (listenerContainers != null && listenerContainers.size() > 0) {
                    for (int i = listenerContainers.size() - 1; i >= 0; i--)
                        if (listenerContainers.get(i).equals(eventListener)) listenerContainers.remove(i);
                }
            }
        }
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
