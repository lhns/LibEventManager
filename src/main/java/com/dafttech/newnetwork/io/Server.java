package com.dafttech.newnetwork.io;

import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Server<P extends Packet> extends AbstractServer<P> {
    private Selector selector;
    private ServerSocketChannel socketChannel;
    private Acceptor acceptor;

    public Server(Class<? extends Protocol<P>> protocolClazz, InetSocketAddress socketAddress, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException, IllegalAccessException, InstantiationException {
        super(Client.class, protocolClazz, receive);

        selector = Selector.open();

        socketChannel = ServerSocketChannel.open();
        socketChannel.socket().bind(socketAddress);
        socketChannel.configureBlocking(false);

        acceptor = new Acceptor();

        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(acceptor);
    }

    @Override
    protected AbstractClient<P> newClientInstance() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        return clientClazz.getConstructor(Class.class, Consumer.class).newInstance(protocolClazz, receive);
    }

    private static class Acceptor {
    }
}
