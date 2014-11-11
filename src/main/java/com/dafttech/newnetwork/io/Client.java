package com.dafttech.newnetwork.io;

import com.dafttech.autoselector.AutoSelector;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.BiConsumer;

public class Client<P extends Packet> extends AbstractClient<P> {
    private final SocketChannel socketChannel;

    public Client(Class<? extends AbstractProtocol> protocolClazz, SocketChannel socketChannel, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        super(protocolClazz, receive);
        this.socketChannel = socketChannel;

        AutoSelector.instance.register(socketChannel, SelectionKey.OP_READ | SelectionKey.OP_WRITE, (selectionKey) -> {
            if ((selectionKey.interestOps() & SelectionKey.OP_READ) > 0)
                read(socketChannel);
            if ((selectionKey.interestOps() & SelectionKey.OP_WRITE) > 0)
                write(socketChannel);
        });
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        this(protocolClazz, toSocketChannel(socketAddress), receive);
    }

    private static SocketChannel toSocketChannel(InetSocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open(socketAddress);
        socketChannel.configureBlocking(false);
        return socketChannel;
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, String host, int port, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        this(protocolClazz, new InetSocketAddress(host, port), receive);
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, String host, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        this(protocolClazz, host.split(":")[0], Integer.parseInt(host.split(":")[1]), receive);
    }

    @Override
    public void close() throws IOException {
        super.close();
        socketChannel.close();
    }
}
