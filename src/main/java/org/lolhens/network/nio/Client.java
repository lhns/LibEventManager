package org.lolhens.network.nio;

import org.lolhens.autoselector.SelectorManager;
import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Client<P> extends AbstractClient<P> {
    private SocketChannel socketChannel;
    private SelectionKey selectionKey;

    public Client(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
        setExceptionHandler(new ExceptionHandler());
    }

    public void setSocketChannel(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);

        int ops = SelectionKey.OP_CONNECT;
        if (socketChannel.isConnected()) ops ^= finishConnect();

        selectionKey = SelectorManager.instance.register(socketChannel, ops, (selectionKey) -> {
            if (!isAlive() || selectionKey != Client.this.selectionKey) return;

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
    public void connect(SocketAddress socketAddress) throws IOException {
        setSocketChannel(SocketChannel.open());

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
        while (isWriting()) {
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException e) {
            }
        }
        socketChannel.close();
        selectionKey.cancel();
        selectionKey.selector().wakeup();
    }
}
