package com.dafttech.autoselector;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * Created by LolHens on 11.11.2014.
 */
class AutoSelector implements Runnable {
    protected Selector selector;

    public AutoSelector(ExecutorService executorService) throws IOException {
        selector = Selector.open();
        executorService.submit(this);
    }

    public SelectionKey register(SelectableChannel channel, int ops, Object att) throws IOException {
        channel.configureBlocking(false);
        synchronized (selector) {
            selector.wakeup();
            return channel.register(selector, ops, att);
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted() && selector.isOpen()) {
                synchronized (selector) {
                }

                if (selector.select(100) > 0) {
                    Set<SelectionKey> readyKeys = selector.selectedKeys();
                    Iterator<SelectionKey> readyKeysIterator = readyKeys.iterator();
                    while (readyKeysIterator.hasNext()) {
                        SelectionKey key = readyKeysIterator.next();
                        readyKeysIterator.remove();
                        if (key.isValid()) ((Consumer<SelectionKey>) key.attachment()).accept(key);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}