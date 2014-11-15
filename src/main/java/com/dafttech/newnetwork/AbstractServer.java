package com.dafttech.newnetwork;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    protected final List<AbstractClient<P>> clients = new LinkedList<>();

    private Consumer<AbstractClient<P>> acceptHandler = null;

    public AbstractServer(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<AbstractClient<P>, P> receiveHandler) {
        super(protocolClazz, receiveHandler);
    }

    @Override
    public final void setProtocol(Class<? extends AbstractProtocol> protocolClazz) {
        super.setProtocol(protocolClazz);
    }

    public final void setAcceptHandler(Consumer<AbstractClient<P>> acceptHandler) {
        this.acceptHandler = acceptHandler;
    }

    protected final Consumer<AbstractClient<P>> getAcceptHandler() {
        return acceptHandler;
    }

    protected final void onAccept(AbstractClient<P> client) {
        if (acceptHandler != null) acceptHandler.accept(client);
    }

    public void broadcast(P packet) {
        synchronized (clients) {
            for (AbstractClient<P> client : clients) client.send(packet);
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        synchronized (clients) {
            for (AbstractClient<P> client : clients) client.close();
        }
    }
}
