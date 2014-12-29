package org.lolhens.network.nio;

import org.lolhens.autoselector.SelectorManager;
import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;
import org.lolhens.network.AbstractServer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server<P> extends AbstractServer<P> {
    public Server(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
        clients = new CopyOnWriteArrayList<>();
        setClientFactory((protocol) -> new Client<P>(protocol));
        setExceptionHandler(new ExceptionHandler());
    }

    @Override
    public void setSocketChannel(ServerSocketChannel socketChannel) throws IOException {
        super.setSocketChannel(socketChannel);

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey) -> {
            if (!isAlive()) return;

            if (selectionKey.isAcceptable()) {
                try {
                    AbstractClient<P> client = newClient(getProtocol());
                    client.setSocketChannel(getSocketChannel().accept());
                    onAccept(client);
                    clients.add(client);
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
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

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        getSocketChannel().close();
    }
}
