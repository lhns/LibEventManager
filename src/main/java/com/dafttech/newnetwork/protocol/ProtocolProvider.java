package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

import java.util.function.Consumer;

public abstract class ProtocolProvider<P extends Packet> extends Protocol<P> {
    private boolean closed = false;

    protected final Protocol<P> protocol;

    public ProtocolProvider(Class<? extends Protocol<P>> protocolClazz) throws IllegalAccessException, InstantiationException {
        this.protocol = protocolClazz.newInstance();
        this.protocol.protocolProvider = this;
    }

    public Class<? extends Protocol<P>> getProtocolClazz() {
        return (Class<? extends Protocol<P>>) protocol.getClass();
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
