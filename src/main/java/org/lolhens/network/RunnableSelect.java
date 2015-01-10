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
            if (selectionKeyContainer.isValid()) {
                try {
                    selectionKeyContainer.getSelectHandler().onSelect(selectionKeyContainer, readyOps);
                    selectionKeyContainer.setActiveOps(0xFFFFFFFF, readyOps);
                } catch (CancelledKeyException e) {
                    //e.printStackTrace();
                }
            }
        }
    }
}