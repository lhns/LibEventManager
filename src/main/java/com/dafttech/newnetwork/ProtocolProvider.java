package com.dafttech.newnetwork;

import com.dafttech.newnetwork.disconnect.DisconnectReason;
import com.dafttech.newnetwork.disconnect.Quit;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;

public abstract class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private BiConsumer<AbstractClient<P>, P> receiveHandler = null;
    private BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler = null;
    private AbstractExceptionHandler exceptionHandler = null;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler, BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) {
        setReceiveHandler(receiveHandler);
        setDisconnectHandler(disconnectHandler);
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


    public final void setDisconnectHandler(BiConsumer<ProtocolProvider<P>, DisconnectReason> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    protected BiConsumer<ProtocolProvider<P>, DisconnectReason> getDisconnectHandler() {
        return disconnectHandler;
    }

    protected final void onDisconnect(DisconnectReason reason) {
        if (disconnectHandler != null) disconnectHandler.accept(this, reason);
    }


    protected final void setExceptionHandler(AbstractExceptionHandler exceptionHandler) {
        if (this.exceptionHandler != null) this.exceptionHandler.protocolProvider = null;
        exceptionHandler.protocolProvider = this;
        this.exceptionHandler = exceptionHandler;
    }

    protected final AbstractExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    protected final void onException(IOException exception) {
        if (exceptionHandler != null) exceptionHandler.handle(exception);
        else throw new RuntimeException(exception);
    }


    @Override
    public void close() throws IOException {
        closed = true;
        onDisconnect(new Quit(this, null));
    }

    public boolean isAlive() {
        return !closed;
    }
}
