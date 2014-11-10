package com.dafttech.newnetwork.io;

import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

public class Server<P extends Packet> extends AbstractServer<P> {
    private final ServerSocketChannel serverSocketChannel;

    @SuppressWarnings("unused")
    private final RunnableSelector first;

    public Server(Class<? extends Protocol<P>> protocolClazz, InetSocketAddress socketAddress) throws IOException, IllegalAccessException, InstantiationException {
        super(Client.class, protocolClazz);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(null, 0);

        first = null;
    }

    @SuppressWarnings("unused")
    private void register(SelectableChannel channel, int ops, Object attachment) {
        // channel.register(null, ops, attachment);
    }

    @SuppressWarnings("unused")
    private class RunnableSelector implements Runnable {
        private final Selector selector;

        private final RunnableSelector prev;
        private volatile RunnableSelector next;

        private RunnableSelector(RunnableSelector parent) {
            prev = parent;
            selector = null;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    if (selector.select() > 0) {
                        Set<SelectionKey> keys = selector.selectedKeys();
                        for (SelectionKey key : keys) {
                            Object attachment = key.attachment();
                            if (attachment != null && attachment instanceof Runnable) {
                                ((Runnable) attachment).run();
                            }
                        }
                        keys.clear();
                    }
                }
            } catch (IOException e) {
                try {
                    selector.close();
                } catch (IOException e1) {

                }
            }
        }
    }

}
