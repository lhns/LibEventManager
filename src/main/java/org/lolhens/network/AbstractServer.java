package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    protected List<AbstractClient<P>> clients;

    private Consumer<AbstractClient<P>> acceptHandler = null;

    public AbstractServer(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public abstract void bind(SocketAddress socketAddress);

    public void bind(int port) {
        bind(new InetSocketAddress(port));
    }

    public void bind(String port) {
        bind(Integer.valueOf(port));
    }

    @Override
    public final void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        super.setProtocol(protocolClazz);
    }


    public final void setAcceptHandler(Consumer<AbstractClient<P>> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

    protected final Consumer<AbstractClient<P>> getAcceptHandler() {
        return acceptHandler;
    }

    protected final void onAccept(AbstractClient<P> client) {
        if (acceptHandler != null) acceptHandler.accept(client);
    }


    public void broadcast(P packet) {
        for (AbstractClient<P> client : clients) client.send(packet);
    }

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        for (AbstractClient<P> client : clients) client.close();
    }
}
