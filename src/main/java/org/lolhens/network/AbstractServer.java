package org.lolhens.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    protected List<AbstractClient<P>> clients;

    private Consumer<AbstractClient<P>> acceptHandler = null;
    private IClientFactory<P> clientFactory = null;

    public AbstractServer(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public abstract void setSocketChannel(ServerSocketChannel socketChannel) throws IOException;

    public abstract void bind(SocketAddress socketAddress) throws IOException;

    public void bind(int port) throws IOException {
        bind(new InetSocketAddress(port));
    }

    public void bind(String port) throws IOException {
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


    public final void setClientFactory(IClientFactory<P> clientFactory) {
        this.clientFactory = clientFactory;
    }

    protected final AbstractClient<P> newClient(Class<? extends AbstractProtocol<P>> protocol) {
        if (clientFactory != null) return clientFactory.newClient(protocol);
        return null;
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
