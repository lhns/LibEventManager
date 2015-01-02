package org.lolhens.network;

import java.nio.channels.CancelledKeyException;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableSelect implements Runnable {
    private final SelectionKeyContainer selectionKeyContainer;
    private final int readyOps;

    public RunnableSelect(SelectionKeyContainer selectionKeyContainer, int readyOps) {
        this.selectionKeyContainer = selectionKeyContainer;
        this.readyOps = readyOps;
    }

    @Override
    public void run() {
        synchronized (selectionKeyContainer) {
            if (selectionKeyContainer.getSelectionKey().isValid()) {
                try {
                    selectionKeyContainer.getSelectHandler().onSelect(selectionKeyContainer, readyOps);
                } catch (CancelledKeyException e) {
                    e.printStackTrace();
                }
            }
        }
        //selectionKeyContainer.setActiveOps(0xFFFFFFFF, readyOps);
    }
}