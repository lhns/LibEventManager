package com.dafttech.eventmanager;

import java.util.List;

public class AsyncEventThread extends Thread {
    private volatile Event event;
    private volatile List<ListenerContainer> eventListenerContainers;

    protected AsyncEventThread(Event event, List<ListenerContainer> eventListenerContainers) {
        this.event = event;
        this.eventListenerContainers = eventListenerContainers;
        start();
    }

    @Override
    public final void run() {
        event.schedule(eventListenerContainers);
    }
}
