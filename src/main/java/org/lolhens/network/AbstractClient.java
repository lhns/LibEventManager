package org.lolhens.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.Executor;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    private AbstractServer<P> server;

    private volatile SocketChannel socketChannel;

    private volatile AbstractProtocol<P> protocol;

    private volatile boolean connected = false;
    private volatile boolean writing = false;

    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public abstract void connect(SocketAddress socketAddress) throws IOException;

    public void connect(String hostname, int port) throws IOException {
        connect(new InetSocketAddress(hostname, port));
    }

    public void connect(String hostname) throws IOException {
        int colonIndex = hostname.lastIndexOf(":");
        if (colonIndex < 0) throw new RuntimeException("Wrong Hostname format: No port specified!");

        String name = hostname.substring(0, colonIndex);

        String port = hostname.substring(colonIndex + 1);
        if (!port.matches("-?\\d+")) throw new RuntimeException("Wrong Hostname format: Port is not numeric!");

        System.out.println(name);

        connect(name, Integer.valueOf(port));
    }

    protected final void onConnect() {
        setConnected(true);

        onConnect(this);
    }

    protected final void receive(P packet) {
        onReceive(this, packet);
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

    // Setters

    protected final void setServer(AbstractServer<P> server) {
        if (this.server != null) throw new UnsupportedOperationException("Cannot override server!");
        this.server = server;
    }

    public void setSocketChannel(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;

        socketChannel.configureBlocking(false);

        setConnected(false);
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

    private final void setConnected(boolean connected) {
        this.connected = connected;
    }

    protected void setWriting(boolean value) {
        writing = value;
    }

    @Override
    protected void setClosed() throws IOException {
        super.setClosed();
        getSocketChannel().close();
        setConnected(false);
        protocol.setClosed();
        if (server != null) server.removeClient(this);
    }

    // Getters

    public final AbstractServer<P> getServer() {
        return server;
    }

    public final SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public boolean isConnected() {
        return connected;
    }

    @Override
    protected final Executor getExecutor() {
        if (server != null) return server.getExecutor();
        return super.getExecutor();
    }

    @Override
    protected final SelectorThread getSelectorThread() {
        if (server != null) return server.getSelectorThread();
        return super.getSelectorThread();
    }

    public final boolean isWriting() {
        return writing;
    }
}
