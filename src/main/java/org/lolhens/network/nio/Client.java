package org.lolhens.network.nio;

import org.lolhens.autoselector.SelectorManager;
import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;
import org.lolhens.network.ProtocolProvider;
import org.lolhens.network.disconnect.DisconnectReason;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.BiConsumer;

public class Client<P> extends AbstractClient<P> {
    protected final SocketChannel socketChannel;
    private final SelectionKey selectionKey;

    protected Client(Class<? extends AbstractProtocol> protocolClazz, SocketChannel socketChannel, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) throws IOException {
        super(protocolClazz, receiveHandler, disconnectHandler);

        setExceptionHandler(new ExceptionHandler());

        this.socketChannel = socketChannel;

        int ops = SelectionKey.OP_CONNECT;
        if (socketChannel.isConnected()) ops ^= finishConnect();

        selectionKey = SelectorManager.instance.register(socketChannel, ops, (selectionKey) -> {
            if (!isAlive()) return;
            if (selectionKey.isReadable()) {
                read(socketChannel);
            }
            if (selectionKey.isWritable()) {
                write(socketChannel);
            }
            if (selectionKey.isConnectable()) {
                selectionKey.interestOps(selectionKey.interestOps() ^ finishConnect());
                selectionKey.selector().wakeup();
            }
        });
    }

    public Client(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) throws IOException {
        this(protocolClazz, toSocketChannel(), receiveHandler, disconnectHandler);
    }

    private static SocketChannel toSocketChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        return socketChannel;
    }

    private final int finishConnect() {
        try {
            socketChannel.finishConnect();
        } catch (IOException e) {
            onException(e);
        }
        onConnect();
        return SelectionKey.OP_CONNECT | SelectionKey.OP_READ;
    }

    @Override
    public void connect(SocketAddress socketAddress) {
        try {
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            onException(e);
        }
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
    protected void onClose() throws IOException {
        super.onClose();
        socketChannel.close();
        selectionKey.cancel();
        selectionKey.selector().wakeup();
    }
}
