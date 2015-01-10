package org.lolhens.network;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableAccept<P> implements Runnable {
    private final IHandlerAccept<P> acceptHandler;
    private final AbstractClient<P> client;

    public RunnableAccept(IHandlerAccept<P> acceptHandler, AbstractClient<P> client) {
        this.acceptHandler = acceptHandler;
        this.client = client;
    }

    @Override
    public void run() {
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        currentThread.setName(name + " (accept)");

        acceptHandler.onAccept(client);

        currentThread.setName(name);
    }
}
