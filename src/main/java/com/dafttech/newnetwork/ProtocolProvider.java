package com.dafttech.newnetwork;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    protected final BiConsumer<ProtocolProvider<P>, P> receiveHandler;
    protected Consumer<IOException> exceptionHandler = null;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receiveHandler) {
        this.receiveHandler = receiveHandler;
        setProtocol(protocolClazz);
    }

    public final Class<? extends AbstractProtocol<P>> getProtocol() {
        return protocolClazz;
    }

    public void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
    }

    protected final void handleException(IOException e) {
        if (exceptionHandler != null) exceptionHandler.accept(e);
        else throw new RuntimeException(e);
    }

    public final void setExceptionHandler(Consumer<IOException> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
