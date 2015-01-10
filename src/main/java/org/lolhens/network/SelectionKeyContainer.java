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

    private final ReadWriteLock interestOpsLock = new ReentrantReadWriteLock();

    protected SelectionKeyContainer(IHandlerSelect selectHandler) {
        this.selectHandler = selectHandler;
    }

    public final void toggleInterestOps(int ops) {
        interestOpsLock.writeLock().lock();
        {
            setInterestOps(interestOps ^ ops, ops);
        }
        interestOpsLock.writeLock().unlock();
    }

    private final void updateOps() {
        selectionKey.interestOps(interestOps & activeOps);
        selectionKey.selector().wakeup();
    }

    // Setters

    protected final void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        interestOps = selectionKey.interestOps();
    }

    protected final void setActiveOps(int ops, int mask) {
        interestOpsLock.writeLock().lock();
        {
            activeOps = (activeOps & ~mask) | (ops & mask);
            updateOps();
        }
        interestOpsLock.writeLock().unlock();
    }

    public final void setInterestOps(int ops, int mask) {
        interestOpsLock.writeLock().lock();
        {
            interestOps = (interestOps & ~mask) | (ops & mask);
            updateOps();
        }
        interestOpsLock.writeLock().unlock();
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

        interestOpsLock.readLock().lock();
        {
            ret = interestOps;
        }
        interestOpsLock.readLock().unlock();

        return ret;
    }
}
