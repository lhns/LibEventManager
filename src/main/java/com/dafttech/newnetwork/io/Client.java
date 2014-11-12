package com.dafttech.newnetwork.io;

import com.dafttech.autoselector.SelectorManager;
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

        SelectorManager.instance.register(socketChannel, SelectionKey.OP_CONNECT | SelectionKey.OP_READ | SelectionKey.OP_WRITE, (selectionKey) -> {
            if (selectionKey.isReadable()) {
                System.out.println("READ");
                read(socketChannel);
            }
            if (selectionKey.isWritable()) {
                System.out.println("WRITE");
                write(socketChannel);
            }
            if (selectionKey.isConnectable()) {
                System.out.println("CONNECT");
                selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                selectionKey.selector().wakeup();
            }
        });
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<ProtocolProvider<P>, P> receive) throws IOException {
        this(protocolClazz, toSocketChannel(socketAddress), receive);
    }

    private static SocketChannel toSocketChannel(InetSocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(socketAddress);
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
