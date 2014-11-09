package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

public abstract class AbstractClient<P extends Packet> extends ProtocolProvider<P> {
    protected AbstractServer<P> server = null;

    public AbstractClient(Class<? extends Protocol<P>> protocolClazz) throws InstantiationException, IllegalAccessException {
        super(protocolClazz);
    }

    protected final void read(byte[] bytes) {
        decode(bytes, this::receive);
    }

    protected abstract void write(byte[] bytes);

    public final void send(P packet) {
        encode(packet, this::write);
    }

    public abstract void receive(P packet);
}
