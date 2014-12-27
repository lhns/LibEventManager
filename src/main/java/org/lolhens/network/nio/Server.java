package org.lolhens.network.nio;

import org.lolhens.autoselector.SelectorManager;
import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;
import org.lolhens.network.AbstractServer;
import org.lolhens.network.ProtocolProvider;
import org.lolhens.network.disconnect.DisconnectReason;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;

public class Server<P> extends AbstractServer<P> {
    protected final ServerSocketChannel socketChannel;

    public Server(Class<? extends AbstractProtocol> protocolClazz) throws IOException {
        super(protocolClazz);

        clients = new CopyOnWriteArrayList<>();

        setExceptionHandler(new ExceptionHandler());

        socketChannel = ServerSocketChannel.open();

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey) -> {
            if (!isAlive()) return;

            if (selectionKey.isAcceptable()) {
                try {
                    AbstractClient<P> client = new Client<P>(protocolClazz, socketChannel.accept());
                    client.setReceiveHandler(getReceiveHandler());
                    client.setDisconnectHandler(getDisconnectHandler());
                    client.setConnectHandler(getConnectHandler());
                    onAccept(client);
                    clients.add(client);
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
    }

    @Override
    public void bind(SocketAddress socketAddress) {
        try {
            socketChannel.bind(socketAddress);
        } catch (IOException e) {
            onException(e);
        }
    }

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        socketChannel.close();
    }
}
