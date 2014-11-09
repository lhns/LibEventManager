package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class ProtocolProvider<P extends Packet> extends Protocol<P> {
    private boolean closed = false;

    protected final Protocol<P> protocol;

    public ProtocolProvider(Class<? extends Protocol<P>> protocolClazz) throws IllegalAccessException, InstantiationException {
        this.protocol = protocolClazz.newInstance();
        this.protocol.protocolProvider = this;
    }

    public Class<? extends Protocol<P>> getProtocolClazz() {
        return protocol.getClass();
    }

    @Override
    protected byte[] encode(P packet) {
        return protocol.encode(packet);
    }

    @Override
    protected P decode(byte[] bytes) {
        return protocol.decode(bytes);
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
