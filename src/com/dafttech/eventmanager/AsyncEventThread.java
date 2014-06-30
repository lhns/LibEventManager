package com.dafttech.eventmanager;

public class AsyncEventThread extends Thread {
    private volatile Event event;

    // TODO: Thread Pool
    protected AsyncEventThread(Event event) {
        this.event = event;
        start();
    }

    @Override
    public final void run() {
        event.schedule();
    }
}
