package org.lolhens.network;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * Created by LolHens on 01.01.2015.
 */
public class SelectorThread extends Thread {
    private final Selector selector;
    private final Executor executor;

    public SelectorThread(Executor executor) {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.executor = executor;

        setDaemon(true);
        start();
    }

    public SelectionKeyContainer register(SelectableChannel channel, int ops, IHandlerSelect selectHandler) throws IOException {
        channel.configureBlocking(false);

        SelectionKeyContainer selectionKeyContainer;

        synchronized (selector) {
            selector.wakeup();
            selectionKeyContainer = new SelectionKeyContainer(selectHandler);
            SelectionKey selectionKey = channel.register(selector, ops, selectionKeyContainer);
            selectionKeyContainer.setSelectionKey(selectionKey);
        }

        return selectionKeyContainer;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted() && selector.isOpen()) {
                synchronized (selector) { // See synchronized block in register(SelectableChannel, int, Object)
                }

                if (selector.select(100) > 0) {
                    Set<SelectionKey> readyKeys = selector.selectedKeys();

                    Iterator<SelectionKey> readyKeysIterator = readyKeys.iterator();
                    while (readyKeysIterator.hasNext()) {
                        SelectionKey selectionKey = readyKeysIterator.next();

                        readyKeysIterator.remove();
                        boolean remove = true;

                        if (selectionKey.isValid()) {
                            try {
                                SelectionKeyContainer selectionKeyContainer = (SelectionKeyContainer) selectionKey.attachment();

                                int activeOps = selectionKeyContainer.getActiveOps();
                                int readyOps = selectionKey.readyOps();

                                System.out.println(Integer.toBinaryString(readyOps));
                                System.out.println(Integer.toBinaryString(activeOps));
                                if ((readyOps & ~activeOps) != 0) {
                                    System.out.println(Integer.toBinaryString((readyOps & ~activeOps)));
                                    remove = false;
                                }
                                System.out.println("-");

                                int ops = readyOps & activeOps;
                                //System.out.println(Integer.toBinaryString(activeOps) + " active");
                                //System.out.println(Integer.toBinaryString(ops));
                                if (ops != 0) {
                                    //selectionKeyContainer.setActiveOps(0, ops);
                                    executor.execute(new RunnableSelect(selectionKeyContainer, ops));
                                }
                            } catch (CancelledKeyException e) {
                                e.printStackTrace();
                            }
                        }

                        //if (remove)
                    }
                }
            }

            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
