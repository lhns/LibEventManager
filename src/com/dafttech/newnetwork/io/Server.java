package com.dafttech.newnetwork.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

import com.dafttech.newnetwork.AbstractServer;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;

public class Server<P extends Packet> extends AbstractServer<P> {
    private final ServerSocketChannel serverSocketChannel;

    // private RunnableSelector first;

    public Server(Protocol<P> protocol, InetSocketAddress socketAddress) throws IOException {
        super(protocol);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(socketAddress);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(null, 0);
    }

    @SuppressWarnings("unused")
    private void register(SelectableChannel channel, int ops, Object attachment) {
        // channel.register(null, ops, attachment);
    }

    @SuppressWarnings("unused")
    private static class RunnableSelector implements Runnable {
        private final Selector selector;

        private RunnableSelector() {
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
