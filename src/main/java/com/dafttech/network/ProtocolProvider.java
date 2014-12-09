package com.dafttech.network;

import com.dafttech.network.disconnect.DisconnectReason;
import com.dafttech.network.disconnect.Quit;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private BiConsumer<AbstractClient<P>, P> receiveHandler = null;
    private Consumer<AbstractClient<P>> connectHandler = null;
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

    final void onReceive(AbstractClient<P> client, P packet) {
        if (receiveHandler != null) receiveHandler.accept(client, packet);
    }


    public final void setConnectHandler(Consumer<AbstractClient<P>> connectHandler) {
        this.connectHandler = connectHandler;
    }

    protected final Consumer<AbstractClient<P>> getConnectHandler() {
        return connectHandler;
    }

    final void onConnect(AbstractClient<P> client) {
        if (connectHandler != null) connectHandler.accept(client);
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


    protected void onClose() throws IOException {
        closed = true;
    }


    @Override
    public final void close() throws IOException {
        onClose();
        onDisconnect(new Quit(this, null));
    }

    public boolean isAlive() {
        return !closed;
    }
}
