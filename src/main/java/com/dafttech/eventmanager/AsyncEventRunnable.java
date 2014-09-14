package com.dafttech.eventmanager;

class AsyncEventRunnable implements Runnable {
    private final Event event;

    protected AsyncEventRunnable(Event event) {
        this.event = event;
    }

    @Override
    public final void run() {
        event.schedule();
    }
}
