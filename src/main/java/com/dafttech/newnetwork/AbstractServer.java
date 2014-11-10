package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractServer<P extends Packet> extends ProtocolProvider<P> {
    protected final Class<? extends AbstractClient<P>> clientClazz;
    protected List<AbstractClient<P>> clients = new LinkedList<AbstractClient<P>>();

    public AbstractServer(Class<? extends AbstractClient> clientClazz, Class<? extends Protocol<P>> protocolClazz) throws InstantiationException, IllegalAccessException {
        super(protocolClazz);
        this.clientClazz = (Class<? extends AbstractClient<P>>) clientClazz;
    }
}
