package com.dafttech.newnetwork.nio;

import com.dafttech.autoselector.SelectorManager;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.function.BiConsumer;

public class Server<P extends Packet> extends AbstractServer<P> {
    protected final ServerSocketChannel socketChannel;

    public Server(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<AbstractClient<P>, P> receive) throws IOException {
        super(protocolClazz, receive);

        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(socketAddress);

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey) -> {
            if (!isAlive()) return;

            if (selectionKey.isAcceptable()) {
                try {
                    AbstractClient<P> client = new Client<P>(protocolClazz, socketChannel.accept(), receive);
                    client.setExceptionHandler(getExceptionHandler());
                    onAccept(client);
                    System.out.println("add");
                    clients.add(client);
                    System.out.println("added");
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
    }

    public Server(Class<? extends AbstractProtocol> protocolClazz, int port, BiConsumer<AbstractClient<P>, P> receiveHandler) throws IOException {
        this(protocolClazz, new InetSocketAddress(port), receiveHandler);
    }

    @Override
    public void close() throws IOException {
        super.close();
        socketChannel.close();
    }
}
