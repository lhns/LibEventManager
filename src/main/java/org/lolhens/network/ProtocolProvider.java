package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;
import org.lolhens.network.disconnect.Quit;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class ProtocolProvider<P> implements Closeable {
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    private boolean closed = false;
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private IReceiveHandler<P> receiveHandler = null;
    private IConnectHandler<P> connectHandler = null;
    private IDisconnectHandler<P> disconnectHandler = null;
    private AbstractExceptionHandler exceptionHandler = null;

    public ProtocolProvider(Class<? extends AbstractProtocol> protocolClazz) {
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


    public final void setReceiveHandler(IReceiveHandler<P> receiveHandler) {
        this.receiveHandler = receiveHandler;
    }

    protected final IReceiveHandler<P> getReceiveHandler() {
        return receiveHandler;
    }

    final void onReceive(AbstractClient<P> client, P packet) {
        if (receiveHandler != null) receiveHandler.onReceive(client, packet);
    }


    public final void setConnectHandler(IConnectHandler<P> connectHandler) {
        this.connectHandler = connectHandler;
    }

    protected final IConnectHandler<P> getConnectHandler() {
        return connectHandler;
    }

    final void onConnect(AbstractClient<P> client) {
        if (connectHandler != null) connectHandler.onConnect(client);
    }


    public final void setDisconnectHandler(IDisconnectHandler<P> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    protected IDisconnectHandler<P> getDisconnectHandler() {
        return disconnectHandler;
    }

    protected final void onDisconnect(DisconnectReason reason) {
        if (disconnectHandler != null) disconnectHandler.onDisconnect(this, reason);
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
