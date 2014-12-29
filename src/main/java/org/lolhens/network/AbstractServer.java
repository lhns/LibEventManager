package org.lolhens.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.List;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    private ServerSocketChannel socketChannel;

    protected List<AbstractClient<P>> clients;
    private IClientFactory<P> clientFactory = null;

    private IAcceptHandler<P> acceptHandler = null;

    public AbstractServer(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public void setSocketChannel(ServerSocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
    }

    public ServerSocketChannel getSocketChannel() {
        return socketChannel;
    }


    protected final AbstractClient<P> newClient(Class<? extends AbstractProtocol<P>> protocol) {
        if (clientFactory != null) {
            AbstractClient<P> client = clientFactory.newClient(protocol);
            client.setReceiveHandler(getReceiveHandler());
            client.setConnectHandler(getConnectHandler());
            client.setDisconnectHandler(getDisconnectHandler());
            return client;
        }
        return null;
    }

    protected final void removeClient(AbstractClient<P> client) {
        clients.remove(client);
    }

    public final void setClientFactory(IClientFactory<P> clientFactory) {
        this.clientFactory = clientFactory;
    }


    public final void setAcceptHandler(IAcceptHandler<P> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

    protected final IAcceptHandler<P> getAcceptHandler() {
        return acceptHandler;
    }

    protected final void onAccept(AbstractClient<P> client) {
        if (acceptHandler != null) acceptHandler.onAccept(client);
    }


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

    public void broadcast(P packet) {
        for (AbstractClient<P> client : clients) client.send(packet);
    }

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        for (AbstractClient<P> client : clients) client.close();
    }
}
