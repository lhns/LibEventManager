package org.lolhens.network;

import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableSelect implements Runnable {
    private final IHandlerSelect selectHandler;
    private final SelectionKey selectionKey;
    private final int readyOps;

    public RunnableSelect(IHandlerSelect selectHandler, SelectionKey selectionKey, int readyOps) {
        this.selectHandler = selectHandler;
        this.selectionKey = selectionKey;
        this.readyOps = readyOps;
    }

    @Override
    public void run() {
        if (selectionKey.isValid()) {
            try {
                int switchOps = selectHandler.onSelect(selectionKey, readyOps);
                if (switchOps != 0) {
                    selectionKey.interestOps(selectionKey.interestOps() ^ switchOps);
                    selectionKey.selector().wakeup();
                }
            } catch (CancelledKeyException e) {
            }
        }
    }
}