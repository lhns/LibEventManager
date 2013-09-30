package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.List;

public class AsyncEventQueue {
    volatile protected List<Event> eventQueue;
    volatile private Thread queueThread;
    volatile private int queueOverflow = 2000;

    protected AsyncEventQueue() {
        eventQueue = new ArrayList<Event>();
        queueThread = new QueueThread(this);
    }

    protected void add(Event event) throws QueueOverflowException {
        if (eventQueue.size() >= queueOverflow) throw new QueueOverflowException();
        eventQueue.add(event);
        notifyThread();
    }

    private void notifyThread() {
        queueThread.start();
    }
}
