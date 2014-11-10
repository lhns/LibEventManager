package com.dafttech.newnetwork.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ProtocolProvider<P> extends Protocol<P> {
    private boolean closed = false;

    protected final Protocol<P> protocol;
    protected final BiConsumer<ProtocolProvider<P>, P> receive;

    public final Class<? extends Protocol<P>> protocolClazz;

    public ProtocolProvider(Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws IllegalAccessException, InstantiationException {
        this.protocolClazz = (Class<? extends Protocol<P>>) protocolClazz;
        this.receive = receive;

        this.protocol = this.protocolClazz.newInstance();
        this.protocol.protocolProvider = this;
    }

    @Override
    protected void encode(P packet, OutputStream outputStream) {
        protocol.encode(packet, outputStream);
    }

    @Override
    protected void decode(InputStream inputStream, Consumer<P> submitPacket) {
        protocol.decode(inputStream, submitPacket);
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
