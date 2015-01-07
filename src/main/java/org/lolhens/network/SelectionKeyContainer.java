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
            int changedOps = (activeOps ^ ops) & mask; // save changed ops with mask
            activeOps = (activeOps & ~mask) | (ops & mask); // save active ops with mask
            bufferedOps = (bufferedOps & ~changedOps) | (activeOps & changedOps); // set changed ops to active ops
            if (changedOps != 0)
                setInterestOps(bufferedOps, changedOps); // set interest ops to buffered ops with changedOps as mask
        }
        activeOpsLock.writeLock().unlock();
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
        bufferedOps |= ops & ~activeOps;//buffer them if they are not active
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
