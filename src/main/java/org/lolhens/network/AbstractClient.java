package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    private AbstractProtocol<P> protocol;

    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz) {
        super(protocolClazz);
    }

    public abstract void connect(SocketAddress socketAddress);

    public void connect(String hostname, int port) {
        connect(new InetSocketAddress(hostname, port));
    }

    public void connect(String hostname) {
        String[] split = hostname.split(":");
        if (split.length != 2) throw new RuntimeException("Wrong Hostname format!");
        connect(split[0], Integer.valueOf(split[1]));
    }

    @Override
    public final void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        super.setProtocol(protocolClazz);
        try {
            AbstractProtocol<P> newProtocol = getProtocol().newInstance();
            newProtocol.client = this;
            if (protocol != null) protocol.client = null;
            protocol = newProtocol;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Protocol instantiation failed!", e);
        }
    }

    protected final void onConnect() {
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

    protected abstract void setWriteEnabled(boolean value);

    @Override
    protected void onClose() throws IOException {
        super.onClose();
        protocol.onClose();
    }
}
