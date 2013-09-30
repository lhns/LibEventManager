package com.dafttech.eventmanager;

import java.util.ArrayList;
import java.util.List;

public class AsyncEventQueue {
    volatile protected List<Event> eventQueue = new ArrayList<Event>();
    volatile protected QueueThread queueThread;
    volatile private int queueOverflow = 2000;

    protected void add(Event event) throws QueueOverflowException {
        if (eventQueue.size() >= queueOverflow) throw new QueueOverflowException();
        eventQueue.add(event);
        sheduleQueue();
    }

    private void sheduleQueue() {
        if (queueThread == null || !queueThread.isRunning()) {
            queueThread = new QueueThread();
        }
    }

    private class QueueThread extends Thread {
        private volatile boolean running;

        public QueueThread() {
            running = true;
            start();
        }

        @Override
        public void run() {
            Event firstEvent = null;
            while (eventQueue.size() > 0) {
                firstEvent = eventQueue.get(0);
                if (firstEvent != null) firstEvent.sheduleEvent();
                if (firstEvent == null || firstEvent.isDone()) eventQueue.remove(0);
            }
            running = false;
        }

        public boolean isRunning() {
            return running;
        }
    }
}
