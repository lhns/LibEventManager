package com.dafttech.eventmanager;

public class QueueThread extends Thread {
    private AsyncEventQueue asyncEventQueue;
    private boolean running = false;

    public QueueThread(AsyncEventQueue asyncEventQueue) {
        this.asyncEventQueue = asyncEventQueue;
    }

    @Override
    public void run() {
        running = true;
        Event firstEvent = null;
        while (asyncEventQueue.eventQueue.size() > 0) {
            firstEvent = asyncEventQueue.eventQueue.get(0);
            if (firstEvent != null) firstEvent.sheduleEvent();
            if (firstEvent == null || firstEvent.isDone()) asyncEventQueue.eventQueue.remove(0);
        }
        running = false;
    }

    @Override
    public synchronized void start() {
        if (!running) super.start();
    }
}
