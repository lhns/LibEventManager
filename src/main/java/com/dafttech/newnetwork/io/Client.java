package com.dafttech.newnetwork.io;

import com.dafttech.newnetwork.AbstractClient;
import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.util.function.BiConsumer;

public class Client<P extends Packet> extends AbstractClient<P> {
    public Client(Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws IllegalAccessException, InstantiationException {
        super(protocolClazz, receive);
    }

    @Override
    protected void write(byte[] bytes) {

    }
}
