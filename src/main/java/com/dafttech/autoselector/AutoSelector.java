package com.dafttech.autoselector;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by LolHens on 10.11.2014.
 */
public class AutoSelector {
    public static final AutoSelector instance = new AutoSelector();

    private SelectorContainer[] selectorContainers;
    private ExecutorService executorService;

    public AutoSelector() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        selectorContainers = new SelectorContainer[availableProcessors];
        executorService = Executors.newFixedThreadPool(availableProcessors);
    }

    public void newSelector() throws IOException {
        Selector selector = Selector.open();
        //SelectorContainer selectorContainer = new SelectorContainer(selector);
        //executorService.submit(selectorContainer);
    }

    private class SelectorContainer implements Runnable {
        private Selector selector;

        public SelectorContainer() throws IOException {
            selector = Selector.open();
            executorService.submit(this);
        }

        @Override
        public void run() {

        }
    }
}
