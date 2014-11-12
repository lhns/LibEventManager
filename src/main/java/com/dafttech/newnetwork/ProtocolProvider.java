package com.dafttech.newnetwork;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    protected final BiConsumer<ProtocolProvider<P>, P> receive;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) {
        this.receive = receive;
        setProtocol(protocolClazz);
    }

    public final Class<? extends AbstractProtocol<P>> getProtocol() {
        return protocolClazz;
    }

    public void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
    }

    protected void ioException(IOException e) {
        throw new RuntimeException(e);
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
