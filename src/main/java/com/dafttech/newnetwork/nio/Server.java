package com.dafttech.newnetwork.nio;

import com.dafttech.autoselector.SelectorManager;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.function.BiConsumer;

public class Server<P extends Packet> extends AbstractServer<P> {
    protected final ServerSocketChannel socketChannel;

    public Server(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        super(protocolClazz, receive);

        socketChannel = ServerSocketChannel.open();
        socketChannel.bind(socketAddress);

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_ACCEPT, (selectionKey) -> {
            if (selectionKey.isAcceptable()) {
                try {
                    AbstractClient<P> client = new Client<P>(protocolClazz, socketChannel.accept(), receive);
                    onAccept(client);
                    clients.add(client);
                } catch (IOException e) {
                    onException(e);
                }
            }
        });
    }

    public Server(Class<? extends AbstractProtocol> protocolClazz, int port, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        this(protocolClazz, new InetSocketAddress(port), receive);
    }

    @Override
    public void close() throws IOException {
        super.close();
        socketChannel.close();
    }
}
