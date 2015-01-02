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

    private final ReadWriteLock activeOpsLock = new ReentrantReadWriteLock();
    private final ReadWriteLock interestOpsLock = new ReentrantReadWriteLock();

    protected SelectionKeyContainer(IHandlerSelect selectHandler) {
        this.selectHandler = selectHandler;
    }

    // Setters

    protected final void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    protected final void setActiveOps(int ops, int mask) {
        activeOpsLock.writeLock().lock();
        {
            activeOps = (activeOps & ~mask) | (ops & mask);
        }
        activeOpsLock.writeLock().unlock();
    }

    public void setInterestOps(int ops, int mask) {
        interestOpsLock.writeLock().lock();
        {
            selectionKey.interestOps((selectionKey.interestOps() & ~mask) | (ops & mask));
        }
        interestOpsLock.writeLock().unlock();

        selectionKey.selector().wakeup();
    }

    public void toggleInterestOps(int ops) {
        interestOpsLock.writeLock().lock();
        {
            selectionKey.interestOps(selectionKey.interestOps() ^ ops);
        }
        interestOpsLock.writeLock().unlock();

        selectionKey.selector().wakeup();
    }

    public void cancel() {
        selectionKey.cancel();
    }

    // Getters

    protected final IHandlerSelect getSelectHandler() {
        return selectHandler;
    }

    public final SelectionKey getSelectionKey() {
        return selectionKey;
    }

    protected final int getActiveOps() {
        int ret;
        activeOpsLock.readLock().lock();
        {
            ret = activeOps;
        }
        activeOpsLock.readLock().unlock();
        return ret;
    }

    public final boolean isValid() {
        return selectionKey.isValid();
    }

    public final int getInterestOps() {
        int ret;
        interestOpsLock.readLock().lock();
        {
            ret = selectionKey.interestOps();
        }
        interestOpsLock.readLock().unlock();
        return ret;
    }
}
