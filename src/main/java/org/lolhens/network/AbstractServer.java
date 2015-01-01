package org.lolhens.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
import java.util.function.Predicate;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    protected List<AbstractClient<P>> clients;
    private IFactoryClient<P> clientFactory = null;

    private ServerSocketChannel socketChannel;

    private IHandlerAccept<P> acceptHandler = null;

    public AbstractServer(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    protected final void onAccept(AbstractClient<P> client) {
        if (acceptHandler != null) getExecutor().execute(new RunnableAccept<>(acceptHandler, client));
    }

    public abstract void bind(SocketAddress socketAddress) throws IOException;

    public void bind(int port) throws IOException {
        bind(new InetSocketAddress(port));
    }

    public void bind(String port) throws IOException {
        bind(Integer.valueOf(port));
    }

    public void broadcast(Predicate<AbstractClient<P>> include, P packet) {
        for (AbstractClient<P> client : clients) if (include.test(client)) client.send(packet);
    }

    public void broadcast(P packet) {
        broadcast((client) -> true, packet);
    }

    // Setters

    protected final AbstractClient<P> newClient(Class<? extends AbstractProtocol<P>> protocol) {
        if (clientFactory != null) {
            AbstractClient<P> client = clientFactory.newClient(protocol);
            client.setServer(this);
            client.setReceiveHandler(getReceiveHandler());
            client.setConnectHandler(getConnectHandler());
            client.setDisconnectHandler(getDisconnectHandler());
            return client;
        }
        return null;
    }

    public final void setClientFactory(IFactoryClient<P> clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void setSocketChannel(ServerSocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
    }

    public final void setAcceptHandler(IHandlerAccept<P> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        getSocketChannel().close();
        for (AbstractClient<P> client : clients) client.close();
    }

    // Getters

    protected final void removeClient(AbstractClient<P> client) {
        clients.remove(client);
    }

    public ServerSocketChannel getSocketChannel() {
        return socketChannel;
    }

    protected final IHandlerAccept<P> getAcceptHandler() {
        return acceptHandler;
    }
}
