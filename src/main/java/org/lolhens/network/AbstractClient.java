package org.lolhens.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    private AbstractProtocol<P> protocol;
    private AbstractServer<P> server;

    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public abstract void setSocketChannel(SocketChannel socketChannel) throws IOException;

    public abstract void connect(SocketAddress socketAddress) throws IOException;

    public void connect(String hostname, int port) throws IOException {
        connect(new InetSocketAddress(hostname, port));
    }

    public void connect(String hostname) throws IOException {
        String[] split = hostname.split(":");
        if (split.length != 2) throw new RuntimeException("Wrong Hostname format!");
        connect(split[0], Integer.valueOf(split[1]));
    }

    @Override
    public final void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        super.setProtocol(protocolClazz);
        try {
            AbstractProtocol<P> newProtocol = getProtocol().newInstance();
            newProtocol.setClient(this);
            if (protocol != null) protocol.setClient(null);
            protocol = newProtocol;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Protocol instantiation failed!", e);
        }
    }

    protected final void setServer(AbstractServer<P> server) {
        this.server = server;
    }

    public final AbstractServer<P> getServer() {
        return server;
    }

    protected final void onConnect() {
        onConnect(this);
    }

    protected final void receive(P packet) {
        onReceive(this, packet);
    }

    public final boolean isWriting() {
        return protocol.isWriteEnabled();
    }

    public final void send(P packet) {
        protocol.send(packet);
    }

    protected final void read(ReadableByteChannel in) {
        try {
            protocol.read(in);
        } catch (IOException e) {
            onException(e);
        }
    }

    protected final void write(WritableByteChannel out) {
        try {
            protocol.write(out);
        } catch (IOException e) {
            onException(e);
        }
    }

    protected abstract void setWriteEnabled(boolean value);

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        protocol.onClose();
        if (server != null) server.removeClient(this);
    }
}
