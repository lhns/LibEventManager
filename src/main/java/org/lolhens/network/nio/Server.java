package org.lolhens.network.nio;

import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;
import org.lolhens.network.AbstractServer;
import org.lolhens.network.SelectionKeyContainer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server<P> extends AbstractServer<P> {
    private SelectionKeyContainer selectionKeyContainer;

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

        if (selectionKeyContainer != null) selectionKeyContainer.cancel();

        selectionKeyContainer = getSelectorThread().register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey, readyOps) -> {
            if (!isAlive()) return;

            if ((readyOps & SelectionKey.OP_ACCEPT) != 0) {
                try {
                    AbstractClient<P> client = newClient(getProtocol());
                    SocketChannel channel = getSocketChannel().accept();
                    if (channel != null) {
                        client.setSocketChannel(channel);
                        clients.add(client);
                        onAccept(client);
                    }
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        selectionKeyContainer.cancel();
    }

    // Getters
}
