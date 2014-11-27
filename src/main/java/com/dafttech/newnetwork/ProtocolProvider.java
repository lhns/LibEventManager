package com.dafttech.newnetwork;

import com.dafttech.newnetwork.exception.AbstractExceptionProcessor;
import com.dafttech.newnetwork.exception.disconnect.DisconnectReason;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ProtocolProvider<P> implements Closeable {
    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private BiConsumer<AbstractClient<P>, P> receiveHandler = null;
    private AbstractExceptionProcessor exceptionProcessor = null;
    private Consumer<DisconnectReason> disconnectHandler = null;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler) {
        setReceiveHandler(receiveHandler);
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

    protected final void setExceptionProcessor(AbstractExceptionProcessor exceptionProcessor) {
        this.exceptionProcessor = exceptionProcessor;
    }

    protected final AbstractExceptionProcessor getExceptionProcessor() {
        return exceptionProcessor;
    }

    public final void setDisconnectHandler(Consumer<DisconnectReason> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    protected final Consumer<DisconnectReason> getDisconnectHandler() {
        return disconnectHandler;
    }

    protected final void onException(IOException exception) {
        DisconnectReason reason = null;
        if (exceptionProcessor != null) reason = exceptionProcessor.process(this, exception);
        if (disconnectHandler != null && reason != null) disconnectHandler.accept(reason);
        else {
            try {
                close();
            } catch (IOException e) {
            }
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void close() throws IOException {
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
