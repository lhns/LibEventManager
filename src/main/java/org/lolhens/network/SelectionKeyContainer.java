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

    private volatile int interestOps;
    private volatile int activeOps = 0xFFFFFFFF;
    private volatile int bufferedOps = 0xFFFFFFFF;

    private final ReadWriteLock interestOpsLock = new ReentrantReadWriteLock();

    protected SelectionKeyContainer(IHandlerSelect selectHandler) {
        this.selectHandler = selectHandler;
    }

    // Setters

    protected final void setSelectionKey(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        this.interestOps = selectionKey.interestOps();
    }

    protected final void setActiveOps(int ops, int mask) {
        interestOpsLock.writeLock().lock();
        {
            int changedOps = (activeOps ^ ops) & mask;

            activeOps = (activeOps & ~mask) | (ops & mask);

            bufferedOps = bufferedOps | (changedOps & ~activeOps);

            if (changedOps != 0)
                setInterestOps(bufferedOps, changedOps);
        }
        interestOpsLock.writeLock().unlock();
    }

    public final void toggleInterestOps(int ops) {
        interestOpsLock.writeLock().lock();
        {
            setInterestOps(getInterestOps() ^ ops, ops);
        }
        interestOpsLock.writeLock().unlock();
    }

    private final void setInterestOps(int ops, int mask) {
        ops = (interestOps & ~mask) | (ops & mask);

        bufferedOps = (bufferedOps & activeOps) | (ops & ~activeOps);

        interestOps = ops & activeOps;
        selectionKey.interestOps(interestOps);

        selectionKey.selector().wakeup();
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

        //interestOpsLock.readLock().lock();
        {
            ret = (interestOps & activeOps) | (bufferedOps & ~activeOps);
        }
        //interestOpsLock.readLock().unlock();

        return ret;
    }
}
