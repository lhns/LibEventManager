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

    protected void add(Event event) throws QueueOverflowException {
        if (eventQueue.size() >= queueOverflow) throw new QueueOverflowException();
        eventQueue.add(event);
        notifyThread();
    }

    protected void notifyThread() {
        if (queueThread != null) {
            synchronized (queueThread) {
                queueThread.notify();
            }
        }
    }

    protected void waitThread() {
        if (queueThread != null) {
            synchronized (queueThread) {
                try {
                    queueThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * DON'T USE!! You want to use start().
     */
    @Deprecated
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

    /**
     * Set the Queue overflow
     * 
     * @param queueOverflow
     *            int - number of maximum events in this list.
     */
    public void setQueueOverflow(int queueOverflow) {
        this.queueOverflow = queueOverflow;
    }

    /**
     * Starts this queue with a Thread priority.
     * 
     * @param priority
     *            int - Queue Thread priority.
     */
    public void start(int priority) {
        this.priority = priority;
        start();
    }

    /**
     * Starts this queue.
     */
    public void start() {
        running = true;
        if (queueThread != null) {
            queueThread.interrupt();
        }
        queueThread = new Thread(this, "EventManagerAsyncQueue");
        if (!queueThread.isAlive()) {
            queueThread.start();
            queueThread.setPriority(priority);
        }
    }

    /**
     * Stops this queue.
     */
    public void stop() {
        running = false;
        notifyThread();
    }

    /**
     * Sets the Thread priority for this queue.
     * 
     * @param priority
     *            int - Queue Thread priority.
     */
    public void setPriority(int priority) {
        this.priority = priority;
        if (queueThread != null && queueThread.isAlive()) {
            queueThread.setPriority(priority);
        }
        notifyThread();
    }
}
