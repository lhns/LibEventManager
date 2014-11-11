package com.dafttech.autoselector;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Created by LolHens on 10.11.2014.
 */
public class AutoSelector {
    public static final AutoSelector instance = new AutoSelector();

    private SelectorContainer[] selectorContainers;
    private ExecutorService executorService;
    private int next = 0;

    public AutoSelector() {
        selectorContainers = new SelectorContainer[Runtime.getRuntime().availableProcessors()];
        executorService = Executors.newFixedThreadPool(selectorContainers.length);
    }

    public SelectionKey register(SelectableChannel channel, int ops, Consumer<SelectionKey> runnable) throws IOException {
        if (selectorContainers[next] == null) selectorContainers[next] = new SelectorContainer();
        SelectorContainer selectorContainer = selectorContainers[next++];
        if (next >= selectorContainers.length) next = 0;

        SelectionKey selectionKey = channel.register(selectorContainer.selector, ops, runnable);
        selectorContainer.selector.wakeup();

        return selectionKey;
    }

    private class SelectorContainer implements Runnable {
        private Selector selector;

        public SelectorContainer() throws IOException {
            selector = Selector.open();
            executorService.submit(this);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    selector.select();
                    Set<SelectionKey> selected = selector.selectedKeys();
                    for (SelectionKey key : selected)
                        if (key.isValid()) ((Consumer<SelectionKey>) key.attachment()).accept(key);
                    selected.clear();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                selector.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
