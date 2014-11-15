package com.dafttech.newnetwork;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private BiConsumer<AbstractClient<P>, P> receiveHandler = null;
    private Consumer<IOException> exceptionHandler = null;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler) {
        this.receiveHandler = receiveHandler;
        setProtocol(protocolClazz);
    }

    public void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
    }

    public final Class<? extends AbstractProtocol<P>> getProtocol() {
        return protocolClazz;
    }


    public final void setReceiveHandler(BiConsumer<AbstractClient<P>, P> receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

    protected final BiConsumer<AbstractClient<P>, P> getReceiveHandler() {
        return receiveHandler;
    }

    public final void setExceptionHandler(Consumer<IOException> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    protected final Consumer<IOException> getExceptionHandler() {
        return exceptionHandler;
    }

    protected final void onException(IOException e) {
        if (exceptionHandler != null) exceptionHandler.accept(e);
        else throw new RuntimeException(e);
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
