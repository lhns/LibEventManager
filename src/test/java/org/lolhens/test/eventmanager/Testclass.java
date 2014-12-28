package org.lolhens.test.eventmanager;

import org.lolhens.eventmanager.Event;
import org.lolhens.eventmanager.EventListener;

public class Testclass {

    @EventListener.Filter("filter2")
    public static int somefilter = 3;

    @EventListener("test1")
    private Testclass(Event event) {
        event.out.add(event.getEventType() + " testclass constructor priority="
                + event.getCurrentListenerContainer().getPriority());
    }

    public static void main(String[] args) {
        Events.eventmanager.registerEventListener(Testclass.class);
        Events.eventmanager.registerEventListener(new Secondclass());

        Events.eventmanager.callSync(Events.testevent1, 3).out.forEach(System.out::println);
        Events.eventmanager.callSync(Events.testevent2, 5).out.forEach(System.out::println);
    }

    @EventListener("test1")
    private static void staticlistener(Event event, Object randomobject, int someint) {
        event.out.add(event.getEventType() + " testclass listener (static) (test1) priority="
                + event.getCurrentListenerContainer().getPriority());
    }

    @EventListener(value = "test1", filter = "filter2", priority = 1)
    public static void staticListener2(Event event) {
        event.out.add(event.getEventType() + " testclass listener2 (static) (test1) priority="
                + event.getCurrentListenerContainer().getPriority());
    }

    @EventListener.Filter("filter1")
    public static int aFilter() {
        return 5;
    }
}
