package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

public abstract class AbstractClient<P extends Packet> extends ProtocolProvider<P> {
    public AbstractClient(Class<? extends Protocol<P>> protocolClazz) throws InstantiationException, IllegalAccessException {
        super(protocolClazz);
    }
}
