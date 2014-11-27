package com.dafttech.newnetwork;

import com.dafttech.newnetwork.disconnect.DisconnectReason;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    private AbstractProtocol<P> protocol;

    private Consumer<AbstractClient<P>> connectHandler = null;

    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) {
        super(protocolClazz, receiveHandler, disconnectHandler);
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

    public final void setConnectHandler(Consumer<AbstractClient<P>> connectHandler) {
        this.connectHandler = connectHandler;
    }

    protected final Consumer<AbstractClient<P>> getConnectHandler() {
        return connectHandler;
    }

    protected final void onConnect() {
        if (connectHandler != null) connectHandler.accept(this);
    }

    public final void send(P packet) {
        protocol.send(packet);
    }

    protected final void receive(P packet) {
        getReceiveHandler().accept(this, packet);
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
    public void close() throws IOException {
        super.close();
        protocol.close();
    }

    @Override
    public void finalize() {

    }
}
