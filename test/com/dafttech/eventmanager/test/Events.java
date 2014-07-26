package com.dafttech.eventmanager.test;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventManager;
import com.dafttech.eventmanager.EventType;
import com.dafttech.eventmanager.ListenerContainer;
import com.dafttech.storage.tuple.Tuple;

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
        };
    };
}
