package com.dafttech.eventmanager;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncEvent extends Event {
    private volatile Future<?> future;

    protected AsyncEvent(EventManager eventManager, EventType type, Object[] in, List<ListenerContainer> listenerContainers) {
        super(eventManager, type, in, listenerContainers);
    }

    protected final void setFuture(Future<?> future) {
        this.future = future;
    }

    public AsyncEvent waitForEvent() {
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return this;
    }

    public AsyncEvent waitForEvent(long l, TimeUnit timeunit) {
        try {
            future.get(l, timeunit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return this;
    }
}