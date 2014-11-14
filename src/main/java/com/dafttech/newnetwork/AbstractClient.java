package com.dafttech.newnetwork;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    private AbstractProtocol<P> protocol;

    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receiveHandler) {
        super(protocolClazz, receiveHandler);
    }

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

    public final void send(P packet) {
        protocol.send(packet);
    }

    protected final void receive(P packet) {
        receiveHandler.accept(this, packet);
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

    protected void onConnect() {
    }

    public void close() throws IOException {
        super.close();
        protocol.close();
    }

    @Override
    public void finalize() {
        System.out.println("FINALIZE CLIENT");
    }
}
