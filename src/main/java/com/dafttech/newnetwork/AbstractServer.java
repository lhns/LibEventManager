package com.dafttech.newnetwork;

import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractServer<P> extends ProtocolProvider<P> {
    protected final Class<? extends AbstractClient<P>> clientClazz;
    protected List<AbstractClient<P>> clients = new LinkedList<AbstractClient<P>>();

    public AbstractServer(Class<? extends AbstractClient> clientClazz, Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws InstantiationException, IllegalAccessException {
        super(protocolClazz, receive);
        this.clientClazz = (Class<? extends AbstractClient<P>>) clientClazz;
    }

    protected abstract AbstractClient<P> newClientInstance() throws Exception;

    public void broadcast(P packet) {
        for (AbstractClient<P> client : clients) client.send(packet);
    }
}
