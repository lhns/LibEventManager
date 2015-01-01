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

    public SelectionKey register(SelectableChannel channel, int ops, IHandlerSelect selectHandler) throws IOException {
        channel.configureBlocking(false);

        SelectionKey selectionKey;

        synchronized (selector) {
            selector.wakeup();
            selectionKey = channel.register(selector, ops, selectHandler);
        }

        return selectionKey;
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

                        if (selectionKey.isValid()) {
                            int interestOps = selectionKey.interestOps();
                            int readyOps = selectionKey.readyOps() & interestOps;

                            if (readyOps != 0) {
                                selectionKey.interestOps(interestOps & ~readyOps);

                                try {
                                    Object attachment = selectionKey.attachment();

                                    if (attachment instanceof IHandlerSelect)
                                        executor.execute(new RunnableSelect((IHandlerSelect) attachment, selectionKey, readyOps));
                                } catch (CancelledKeyException e) {
                                }
                            }
                        }

                        readyKeysIterator.remove();
                    }
                }
            }

            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
