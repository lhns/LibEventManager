package com.dafttech.autoselector;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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

    @Override
    public void run() {
        try {
            while (!Thread.interrupted() && selector.isOpen()) {
                if (selector.select() > 0) {
                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (SelectionKey key : selected)
                        if (key.isValid()) ((Consumer<SelectionKey>) key.attachment()).accept(key);
                    selected.clear();
                }
            }
            selector.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}