package com.dafttech.newnetwork.protocol;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ProtocolProvider<P> extends AbstractProtocol<P> {
    private boolean closed = false;

    protected final AbstractProtocol<P> protocol;
    protected final BiConsumer<ProtocolProvider<P>, P> receive;

    public final Class<? extends AbstractProtocol<P>> protocolClazz;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
        this.receive = receive;

        try {
            this.protocol = this.protocolClazz.newInstance();
            this.protocol.protocolProvider = this;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException("Protocol instantiation failed!", e);
        }
    }

    @Override
    protected void encode(P packet, WritableByteChannel out) {
        try {
            protocol.encode(packet, out);
        } catch (IOException e) {
            ioException(e);
        }
    }

    @Override
    protected void decode(ReadableByteChannel in, Consumer<P> submitPacket) {
        try {
            protocol.decode(in, submitPacket);
        } catch (IOException e) {
            ioException(e);
        }
    }

    protected void ioException(IOException e) {
        throw new RuntimeException(e);
    }

    @Override
    public void close() {
        protocol.close();
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
