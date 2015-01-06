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
    private volatile int bufferedOps = 0x00000000;
    // TODO: add changedBufferedOps int

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
        if (bufferedOps != 0) setInterestOps(0xFFFFFFFF, bufferedOps & activeOps);
        if (activeOps != 0) setInterestOps(0x00000000, ~activeOps);
        bufferedOps &= ~activeOps;
    }

    public final void setInterestOps(int ops, int mask) {
        interestOpsLock.writeLock().lock();
        {
            setInterestOps((selectionKey.interestOps() & ~mask) | (ops & mask));
        }
        interestOpsLock.writeLock().unlock();

        selectionKey.selector().wakeup();
    }

    public final void toggleInterestOps(int ops) {
        interestOpsLock.writeLock().lock();
        {
            setInterestOps(selectionKey.interestOps() ^ ops);
        }
        interestOpsLock.writeLock().unlock();

        selectionKey.selector().wakeup();
    }

    private final void setInterestOps(int ops) {
        bufferedOps |= ops & ~activeOps;
        ops &= activeOps;
        selectionKey.interestOps(ops);
    }

    public final void cancel() {
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
