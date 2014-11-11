package com.dafttech.newnetwork;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

public class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;


    protected final BiConsumer<ProtocolProvider<P>, P> receive;

    public final Class<? extends AbstractProtocol<P>> protocolClazz;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
        this.receive = receive;
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
