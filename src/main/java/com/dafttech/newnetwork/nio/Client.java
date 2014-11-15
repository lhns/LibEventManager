package com.dafttech.newnetwork.nio;

import com.dafttech.autoselector.SelectorManager;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.packet.Packet;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.BiConsumer;

public class Client<P extends Packet> extends AbstractClient<P> {
    protected final SocketChannel socketChannel;
    private final SelectionKey selectionKey;

    public Client(Class<? extends AbstractProtocol> protocolClazz, SocketChannel socketChannel, BiConsumer<AbstractClient<P>, P> receiveHandler) throws IOException {
        super(protocolClazz, receiveHandler);

        this.socketChannel = socketChannel;

        int ops = SelectionKey.OP_CONNECT;
        if (socketChannel.isConnected()) ops ^= connect();

        selectionKey = SelectorManager.instance.register(socketChannel, ops, (selectionKey) -> {
            if (!isAlive()) return;

            if (selectionKey.isReadable()) {
                read(socketChannel);
            }
            if (selectionKey.isWritable()) {
                write(socketChannel);
            }
            if (selectionKey.isConnectable()) {
                selectionKey.interestOps(selectionKey.interestOps() ^ connect());
                selectionKey.selector().wakeup();
            }
        });
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, InetSocketAddress socketAddress, BiConsumer<AbstractClient<P>, P> receiveHandler) throws IOException {
        this(protocolClazz, toSocketChannel(socketAddress), receiveHandler);
    }

    private static SocketChannel toSocketChannel(InetSocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(socketAddress);
        return socketChannel;
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, String host, int port, BiConsumer<AbstractClient<P>, P> receiveHandler) throws IOException {
        this(protocolClazz, new InetSocketAddress(host, port), receiveHandler);
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, String host, BiConsumer<AbstractClient<P>, P> receiveHandler) throws IOException {
        this(protocolClazz, host.split(":")[0], Integer.parseInt(host.split(":")[1]), receiveHandler);
    }

    private final int connect() {
        try {
            socketChannel.finishConnect();
        } catch (IOException e) {
            onException(e);
        }
        onConnect();
        return SelectionKey.OP_CONNECT | SelectionKey.OP_READ;
    }

    @Override
    protected void setWriteEnabled(boolean value) {
        if (!selectionKey.isValid()) return;
        int ops = selectionKey.interestOps();
        if (((ops & SelectionKey.OP_WRITE) != 0) != value) {
            selectionKey.interestOps(ops ^ SelectionKey.OP_WRITE);
            selectionKey.selector().wakeup();
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        socketChannel.close();
        selectionKey.cancel();
        selectionKey.selector().wakeup();
    }
}
