package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.util.function.BiConsumer;

public abstract class AbstractClient<P extends Packet> extends ProtocolProvider<P> {
    protected AbstractServer<P> server = null;

    public AbstractClient(Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws InstantiationException, IllegalAccessException {
        super(protocolClazz, receive);
    }

    protected final void read(byte[] bytes) {
        decode(bytes, (packet) -> receive.accept(this, packet));
    }

    protected abstract void write(byte[] bytes);

    public final void send(P packet) {
        encode(packet, this::write);
    }
}
