package com.dafttech.newnetwork.io;

import com.dafttech.autoselector.AutoSelector;
import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.function.BiConsumer;

public class Client<P extends Packet> extends AbstractClient<P> {
    private final SocketChannel socketChannel;

    public Client(Class<? extends Protocol> protocolClazz, SocketChannel socketChannel, BiConsumer<ProtocolProvider<P>, P> receive) throws IllegalAccessException, InstantiationException, IOException {
        super(protocolClazz, receive);
        this.socketChannel = socketChannel;

        AutoSelector.instance.register(socketChannel, SelectionKey.OP_READ, this::read);
    }

    private void read(SelectionKey selectionKey) {
        try {
            read(socketChannel.socket().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected OutputStream getOutputStream() throws IOException, InterruptedException {
        return socketChannel.socket().getOutputStream();
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
