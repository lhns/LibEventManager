package com.dafttech.eventmanager;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.dafttech.filterlist.Blacklist;
import com.dafttech.filterlist.Filterlist;
import com.dafttech.util.ReflectionUtil;

public class EventManager {
    volatile protected Map<EventType, List<ListenerContainer>> registeredListeners = new HashMap<EventType, List<ListenerContainer>>();
    volatile protected Map<String, String> filterShortcuts = new HashMap<String, String>();

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

        boolean isStatic = eventListener.getClass() == Class.class;
        Class<?> eventListenerClass = isStatic ? (Class<?>) eventListener : eventListener.getClass();

        Set<AnnotatedElement> targets = new HashSet<AnnotatedElement>();
        targets.addAll(ReflectionUtil.getAnnotatedMethods(eventListenerClass,
                Arrays.asList(EventListener.class, EventListener.Group.class), null, null));
        targets.addAll(ReflectionUtil.getAnnotatedConstructors(eventListenerClass,
                Arrays.asList(EventListener.class, EventListener.Group.class), null));

        for (AnnotatedElement target : targets) {
            boolean staticTarget = false;
            if (target instanceof Method) {
                staticTarget = Modifier.isStatic(((Method) target).getModifiers());
            } else if (target instanceof Constructor<?>) {
                staticTarget = true;
            }
            if (isStatic && !staticTarget) continue;

            List<EventListener> annotations = new ArrayList<EventListener>();

            if (target.isAnnotationPresent(EventListener.class)) annotations.add(target.getAnnotation(EventListener.class));
            if (target.isAnnotationPresent(EventListener.Group.class))
                Collections.addAll(annotations, target.getAnnotation(EventListener.Group.class).value());

            for (EventListener listenerAnnotation : annotations) {
                String[] filters = listenerAnnotation.filter();
                double priority = listenerAnnotation.priority();

                for (String event : listenerAnnotation.value()) {
                    EventType type = EventType.types.get(event);

                    if (type == null) throw new NoSuchElementException(event);
                    if (!type.isWhitelisted(this)) continue;
                    if (!filterlist.isFiltered(type)) continue;

                    addEventListenerContainer(type, new ListenerContainer(target, eventListener, filters, priority,
                            filterShortcuts));
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
            if (!filterlist.isFiltered(type)) continue;

            listenerContainers = registeredListeners.get(type);
            if (listenerContainers == null || listenerContainers.size() == 0) continue;

            for (int i = listenerContainers.size() - 1; i >= 0; i--)
                if (listenerContainers.get(i).equals(eventListener)) listenerContainers.remove(i);
        }
    }

    public final void unregisterEventListener(Object eventListener) {
        unregisterEventListener(eventListener, new Blacklist<EventType>());
    }

    private final void addEventListenerContainer(EventType type, ListenerContainer newListenerContainer) {
        if (!registeredListeners.containsKey(type)) registeredListeners.put(type, new ArrayList<ListenerContainer>());

        List<ListenerContainer> listenerContainers = registeredListeners.get(type);
        ListenerContainer listenerContainer;
        for (int i = 0; i < listenerContainers.size(); i++) {
            listenerContainer = listenerContainers.get(i);
            if (listenerContainer.equals(newListenerContainer)) return;

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

    public final EventManager addFilterShortcut(String shortcutName, String classPath) {
        filterShortcuts.put(shortcutName, classPath);
        return this;
    }
}
