package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.List;

public class AsyncEventQueue implements Runnable {
    volatile private List<Event> eventQueue = new ArrayList<Event>();
    volatile private int queueOverflow = 2000;
    volatile protected Thread queueThread = null;
    volatile private boolean running = false;
    volatile private int priority = Thread.NORM_PRIORITY;

    protected AsyncEventQueue() {
    }

    protected void add(Event event) throws AsyncEventQueueOverflowException {
        if (eventQueue.size() >= queueOverflow) throw new AsyncEventQueueOverflowException();
        eventQueue.add(event);
        notifyThread();
    }

    protected void notifyThread() {
        synchronized (queueThread) {
            queueThread.notify();
        }
    }

    protected void waitThread() {
        synchronized (queueThread) {
            try {
                queueThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        Event firstEvent = null;
        while (queueThread != null && queueThread.isAlive()) {
            while (eventQueue.size() > 0) {
                firstEvent = eventQueue.get(0);
                if (firstEvent != null) {
                    firstEvent.sheduleEvent();
                    if (firstEvent.isDone()) eventQueue.remove(0);
                }
            }
            if (!running) break;
            waitThread();
        }
    }

    public void setQueueOverflow(int queueOverflow) {
        this.queueOverflow = queueOverflow;
    }

    public void start(int priority) {
        this.priority = priority;
        start();
    }

    public void start() {
        running = true;
        if (queueThread == null) {
            queueThread = new Thread(this, "EventManagerAsyncQueue");
        }
        if (!queueThread.isAlive()) {
            queueThread.start();
            queueThread.setPriority(priority);
        }
    }

    public void stop() {
        running = false;
        notifyThread();
    }

    public void setPriority(int priority) {
        this.priority = priority;
        if (queueThread != null && queueThread.isAlive()) {
            queueThread.setPriority(priority);
        }
        notifyThread();
    }
}
