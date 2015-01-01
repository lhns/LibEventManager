package org.lolhens.network.nio;

import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;
import org.lolhens.network.AbstractServer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server<P> extends AbstractServer<P> {
    private SelectionKey selectionKey;

    public Server(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
        clients = new CopyOnWriteArrayList<>();
        setClientFactory((protocol) -> new Client<P>(protocol));
        setExceptionHandler(new ExceptionHandler());
    }


    @Override
    public void bind(SocketAddress socketAddress) throws IOException {
        setSocketChannel(ServerSocketChannel.open());

        try {
            getSocketChannel().bind(socketAddress);
        } catch (IOException e) {
            onException(e);
        }
    }


    // Setters

    @Override
    public void setSocketChannel(ServerSocketChannel socketChannel) throws IOException {
        super.setSocketChannel(socketChannel);

        if (selectionKey != null) selectionKey.cancel();

        selectionKey = getSelectorThread().register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey, readyOps) -> {
            int switchOps = 0;

            if (!isAlive()) return switchOps;

            if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
                try {
                    AbstractClient<P> client = newClient(getProtocol());
                    client.setSocketChannel(getSocketChannel().accept());
                    onAccept(client);
                    clients.add(client);
                } catch (IOException e) {
                    onException(e);
                }
                switchOps ^= SelectionKey.OP_ACCEPT;
            }

            return switchOps;
        });
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        selectionKey.cancel();
    }

    // Getters
}
