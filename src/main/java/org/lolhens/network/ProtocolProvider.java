package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;
import org.lolhens.network.disconnect.Quit;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class ProtocolProvider<P> implements Closeable {
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private Executor executor;
    private SelectorThread selectorThread;

    private IHandlerReceive<P> receiveHandler = null;
    private IHandlerConnect<P> connectHandler = null;
    private IHandlerDisconnect<P> disconnectHandler = null;
    private AbstractExceptionHandler exceptionHandler = null;

    private boolean closed = false;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz) {
        setReceiveHandler(receiveHandler);
        setDisconnectHandler(disconnectHandler);
        setProtocol(protocolClazz);
    }

    protected final void onReceive(AbstractClient<P> client, P packet) {
        if (receiveHandler != null) getExecutor().execute(new RunnableReceive<>(receiveHandler, client, packet));
    }

    protected final void onConnect(AbstractClient<P> client) {
        if (connectHandler != null) connectHandler.onConnect(client);
    }

    protected final void onDisconnect(DisconnectReason reason) {
        if (disconnectHandler != null) disconnectHandler.onDisconnect(this, reason);
    }

    protected final void onException(IOException exception) {
        if (exceptionHandler != null) exceptionHandler.handle(exception);
        else throw new RuntimeException(exception);
    }


    @Override
    public final void close() throws IOException {
        setClosed();
        onDisconnect(new Quit(this, null));
    }

    // Setters

    public void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        this.protocolClazz = (Class<? extends AbstractProtocol<P>>) protocolClazz;
    }

    public final void setReceiveHandler(IHandlerReceive<P> receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

    public final void setConnectHandler(IHandlerConnect<P> connectHandler) {
        this.connectHandler = connectHandler;
    }

    public final void setDisconnectHandler(IHandlerDisconnect<P> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    protected final void setExceptionHandler(AbstractExceptionHandler exceptionHandler) {
        if (this.exceptionHandler != null) this.exceptionHandler.protocolProvider = null;
        exceptionHandler.protocolProvider = this;
        this.exceptionHandler = exceptionHandler;
    }

    protected void setClosed() throws IOException {
        closed = true;
    }

    // Getters

    public final Class<? extends AbstractProtocol<P>> getProtocol() {
        return protocolClazz;
    }

    protected Executor getExecutor() {
        if (executor == null) executor = Executors.newWorkStealingPool();
        return executor;
    }

    protected SelectorThread getSelectorThread() {
        if (selectorThread == null) selectorThread = new SelectorThread(getExecutor());
        return selectorThread;
    }

    protected final IHandlerReceive<P> getReceiveHandler() {
        return receiveHandler;
    }

    protected final IHandlerConnect<P> getConnectHandler() {
        return connectHandler;
    }

    protected IHandlerDisconnect<P> getDisconnectHandler() {
        return disconnectHandler;
    }

    protected final AbstractExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

    public final boolean isAlive() {
        return !closed;
    }
}
