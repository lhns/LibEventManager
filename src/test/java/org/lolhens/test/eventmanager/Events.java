package org.lolhens.test.eventmanager;

import org.lolhens.eventmanager.Event;
import org.lolhens.eventmanager.EventManager;
import org.lolhens.eventmanager.EventType;
import org.lolhens.eventmanager.ListenerContainer;
import org.lolhens.storage.tuple.Tuple;

public class Events {
    public static final EventManager eventmanager = new EventManager() {
        {
            addFilterShortcut("f", "..Testclass.filter1");
        }
    };

    public static final EventType testevent1 = new EventType("test1", eventmanager);

    public static final EventType testevent2 = new EventType("test2", eventmanager) {
        @Override
        protected boolean isFiltered(Event event, Tuple filter, ListenerContainer eventListener) {
            return event.in.get(0).equals(filter.get(0));
        }
    };
}
