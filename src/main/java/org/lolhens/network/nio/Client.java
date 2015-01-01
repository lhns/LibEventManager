package org.lolhens.network.nio;

import org.lolhens.network.AbstractClient;
import org.lolhens.network.AbstractProtocol;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class Client<P> extends AbstractClient<P> {
    private SelectionKey selectionKey;

    private volatile boolean writeLock = false, writing = false;

    public Client(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
        setExceptionHandler(new ExceptionHandler());
    }

    private final int finishConnect() {
        try {
            getSocketChannel().finishConnect();
        } catch (IOException e) {
            onException(e);
        }
        return SelectionKey.OP_CONNECT | SelectionKey.OP_READ;
    }

    @Override
    public void connect(SocketAddress socketAddress) throws IOException {
        setSocketChannel(SocketChannel.open());

        try {
            getSocketChannel().connect(socketAddress);
        } catch (IOException e) {
            onException(e);
        }
    }

    // Setters

    public void setSocketChannel(SocketChannel socketChannel) throws IOException {
        super.setSocketChannel(socketChannel);

        int ops = SelectionKey.OP_CONNECT;
        if (socketChannel.isConnected()) ops ^= finishConnect();

        if (selectionKey != null) selectionKey.cancel();

        selectionKey = getSelectorThread().register(socketChannel, ops, (selectionKey, readyOps) -> {
            int switchOps = 0;

            if (!isAlive() || selectionKey != Client.this.selectionKey) return switchOps;

            writeLock = true;

            if ((readyOps & SelectionKey.OP_READ) != 0) {
                read(socketChannel);
                switchOps ^= SelectionKey.OP_READ;
            }
            if ((readyOps & SelectionKey.OP_WRITE) != 0) {
                write(socketChannel);
                switchOps ^= SelectionKey.OP_WRITE;
            }
            if ((readyOps & SelectionKey.OP_CONNECT) != 0) {
                switchOps ^= finishConnect();
                onConnect();
                switchOps ^= SelectionKey.OP_CONNECT;
            }

            writeLock = false;

            if (writing) {
                writing = false;
                switchOps ^= SelectionKey.OP_WRITE;
            }

            return switchOps;
        });

        if ((ops & SelectionKey.OP_CONNECT) == 0) onConnect();
    }

    @Override
    protected void setWriting(boolean value) {
        if (!selectionKey.isValid()) return;

        if (isWriting() != value) {
            super.setWriting(value);

            if (writeLock) {
                writing = !writing;
            } else {
                int ops = selectionKey.interestOps();
                if (((ops & SelectionKey.OP_WRITE) != 0) != value) {
                    selectionKey.interestOps(ops ^ SelectionKey.OP_WRITE);
                    selectionKey.selector().wakeup();
                }
            }
        }
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        while (isWriting()) {
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException e) {
            }
        }
        selectionKey.cancel();
    }

    // Getters
}
