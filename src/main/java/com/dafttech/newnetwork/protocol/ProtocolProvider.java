package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ProtocolProvider<P extends Packet> extends Protocol<P> {
    private boolean closed = false;

    protected final Protocol<P> protocol;
    protected final BiConsumer<ProtocolProvider<P>, P> receive;

    public final Class<? extends Protocol<P>> protocolClazz;

    public ProtocolProvider(Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws IllegalAccessException, InstantiationException {
        this.protocolClazz = (Class<? extends Protocol<P>>) protocolClazz;
        this.receive = receive;

        this.protocol = this.protocolClazz.newInstance();
        this.protocol.protocolProvider = this;
    }

    @Override
    protected void encode(P packet, Consumer<byte[]> submitBytes) {
        protocol.encode(packet, submitBytes);
    }

    @Override
    protected void decode(byte[] bytes, Consumer<P> submitPacket) {
        protocol.decode(bytes, submitPacket);
    }

    @Override
    public void close() {
        protocol.close();
        closed = true;
    }

    public boolean isAlive() {
        return !closed;
    }
}
