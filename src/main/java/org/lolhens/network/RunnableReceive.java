package org.lolhens.network;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableReceive<P> implements Runnable {
    private final IHandlerReceive<P> receiveHandler;
    private final AbstractClient<P> client;
    private final P packet;

    public RunnableReceive(IHandlerReceive<P> receiveHandler, AbstractClient<P> client, P packet) {
        this.receiveHandler = receiveHandler;
        this.client = client;
        this.packet = packet;
    }

    @Override
    public void run() {
        receiveHandler.onReceive(client, packet);
    }
}
