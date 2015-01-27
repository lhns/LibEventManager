package org.lolhens.network;

import org.lolhens.network.disconnect.DisconnectReason;
import org.lolhens.network.disconnect.Quit;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

public abstract class ProtocolProvider<P> implements Closeable {
    private Class<? extends AbstractProtocol<P>> protocolClazz;

    private Executor executor;
    private SelectorThread selectorThread;

    private IHandlerReceive<P> receiveHandler = null;
    private IHandlerConnect<P> connectHandler = null;
    private IHandlerDisconnect<P> disconnectHandler = null;
    private AbstractExceptionHandler exceptionHandler = null;

    private volatile boolean closed = false;

    private volatile Object attachment = null;

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

    public final void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    // Getters

    public final Class<? extends AbstractProtocol<P>> getProtocol() {
        return protocolClazz;
    }

    protected Executor getExecutor() {
        if (executor == null) executor = newWorkStealingPool();
        return executor;
    }

    private ExecutorService newWorkStealingPool() {
        return new ForkJoinPool(
                Runtime.getRuntime().availableProcessors(),
                new ForkJoinWorkerThreadFactory(getClass().getSimpleName()),
                null,
                true
        );
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

    public final Object getAttachment() {
        return attachment;
    }

    private static class ForkJoinWorkerThreadFactory implements ForkJoinPool.ForkJoinWorkerThreadFactory {
        private final String prefix;

        public ForkJoinWorkerThreadFactory(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
            ForkJoinWorkerThread thread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            thread.setName(prefix + "-ForkJoinPool-worker-" + nextThreadNum());
            return thread;
        }

        private static int threadInitNumber;

        private static synchronized int nextThreadNum() {
            return threadInitNumber++;
        }
    }
}
