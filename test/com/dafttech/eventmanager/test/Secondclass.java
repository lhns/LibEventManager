package com.dafttech.eventmanager.test;

import com.dafttech.eventmanager.Event;
import com.dafttech.eventmanager.EventListener;

public class Secondclass {
    @EventListener.Group({ @EventListener(value = "test1", filter = "....test.Testclass.filter2"),
            @EventListener(value = "test2", priority = 5, filter = "f") })
    public void listener(Event event) {
        event.out.add(event.getEventType() + " secondclass listener (nonstatic) priority="
                + event.getCurrentListenerContainer().getPriority());
    }

    @EventListener(value = "test2", priority = 1)
    public static void staticListener(Event event) {
        event.out.add(event.getEventType() + " secondclass listener (static) priority="
                + event.getCurrentListenerContainer().getPriority());
    }
}
