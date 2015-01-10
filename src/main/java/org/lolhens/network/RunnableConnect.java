package org.lolhens.network;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableConnect<P> implements Runnable {
    private final IHandlerConnect<P> connectHandler;
    private final AbstractClient<P> client;

    public RunnableConnect(IHandlerConnect<P> connectHandler, AbstractClient<P> client) {
        this.connectHandler = connectHandler;
        this.client = client;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        currentThread.setName(name + " (connect)");

        connectHandler.onConnect(client);

        currentThread.setName(name);
    }
}
