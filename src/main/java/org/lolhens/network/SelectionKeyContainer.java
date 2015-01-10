package org.lolhens.network;

import java.nio.channels.SelectionKey;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by LolHens on 02.01.2015.
 */
public final class SelectionKeyContainer {
    private final IHandlerSelect selectHandler;
    private volatile SelectionKey selectionKey;

    private volatile int activeOps = 0xFFFFFFFF;
    private volatile int interestOps;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected SelectionKeyContainer(IHandlerSelect selectHandler) {
        this.selectHandler = selectHandler;
    }

    public final void toggleInterestOps(int ops) {
        lock.writeLock().lock();
        {
            setInterestOps(interestOps ^ ops, ops);
        }
        lock.writeLock().unlock();
    }

    // Setters

    protected final void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        interestOps = selectionKey.interestOps();
    }

    protected final void setActiveOps(int ops, int mask) {
        lock.writeLock().lock();
        {
            updateOps(interestOps, (activeOps & ~mask) | (ops & mask));
        }
        lock.writeLock().unlock();
    }

    public final void setInterestOps(int ops, int mask) {
        lock.writeLock().lock();
        {
            updateOps((interestOps & ~mask) | (ops & mask), activeOps);
        }
        lock.writeLock().unlock();
    }

    private final void updateOps(int interestOps, int activeOps) {
        int oldOps = this.interestOps & this.activeOps;
        int newOps = interestOps & activeOps;

        this.interestOps = interestOps;
        this.activeOps = activeOps;

        if ((oldOps ^ newOps) != 0) {
            selectionKey.interestOps(newOps);
            selectionKey.selector().wakeup();
        }
    }

    public final void cancel() {
        selectionKey.cancel();
    }

    // Getters

    protected final IHandlerSelect getSelectHandler() {
        return selectHandler;
    }

    protected final int getActiveOps() {
        return activeOps;
    }

    public final boolean isValid() {
        return selectionKey.isValid();
    }

    public final int getInterestOps() {
        int ret;

        lock.readLock().lock();
        {
            ret = interestOps;
        }
        lock.readLock().unlock();

        return ret;
    }
}
