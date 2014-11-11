package com.dafttech.newnetwork.io;

import com.dafttech.autoselector.AutoSelector;
import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.util.function.BiConsumer;

public class Server<P extends Packet> extends AbstractServer<P> {
    private final ServerSocketChannel socketChannel;

    public Server(Class<? extends Protocol<P>> protocolClazz, InetSocketAddress socketAddress, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException, IllegalAccessException, InstantiationException {
        super(Client.class, protocolClazz, receive);

        socketChannel = ServerSocketChannel.open();
        socketChannel.socket().bind(socketAddress);
        socketChannel.configureBlocking(false);

        AutoSelector.instance.register(socketChannel, SelectionKey.OP_ACCEPT, this::accept);
    }

    private void accept(SelectionKey selectionKey) {
        try {
            clients.add(new Client<P>(protocolClazz, socketChannel.accept(), receive));
        } catch (IllegalAccessException | InstantiationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();

        try {
            socketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
