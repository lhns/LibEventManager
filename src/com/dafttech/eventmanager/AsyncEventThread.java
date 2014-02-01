package com.dafttech.eventmanager;

public class AsyncEventThread extends Thread {
    private volatile Event event;

    public AsyncEventThread(Event event) {
        this.event = event;
        start();
    }

    @Override
    public void run() {
        event.schedule();
    }
}
