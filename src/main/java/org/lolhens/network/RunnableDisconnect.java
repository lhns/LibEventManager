package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;

/**
 * Created by LolHens on 01.01.2015.
 */
class RunnableDisconnect<P> implements Runnable {
    private final IHandlerDisconnect<P> disconnectHandler;
    private final ProtocolProvider<P> protocolProvider;
    private final DisconnectReason disconnectReason;

    public RunnableDisconnect(IHandlerDisconnect<P> disconnectHandler, ProtocolProvider<P> protocolProvider, DisconnectReason disconnectReason) {
        this.disconnectHandler = disconnectHandler;
        this.protocolProvider = protocolProvider;
        this.disconnectReason = disconnectReason;
    }

    @Override
    public void run() {
        disconnectHandler.onDisconnect(protocolProvider, disconnectReason);
    }
}
