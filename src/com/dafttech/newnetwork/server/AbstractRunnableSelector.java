package com.dafttech.newnetwork.server;

import java.nio.channels.Selector;

public class AbstractRunnableSelector {
    protected final Selector selector;

    public AbstractRunnableSelector() {
        selector = null;
    }

    public Selector getSelector() {
        return selector;
    }
}
