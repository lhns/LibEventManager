package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class ProtocolProvider<P extends Packet> extends Protocol<P> {
    private boolean closed = false;

    protected final Protocol<P> protocol;

    public ProtocolProvider(Class<? extends Protocol<P>> protocolClazz) throws IllegalAccessException, InstantiationException {
        this.protocol = protocolClazz.newInstance();
    }

    public Class<? extends Protocol<P>> getProtocolClazz() {
        return protocol.getClass();
    }

    @Override
    protected byte[] encode(P packet, ProtocolProvider<P> protocolProvider) {
        return protocol.encode(packet, protocolProvider);
    }

    @Override
    protected P decode(byte[] bytes, ProtocolProvider<P> protocolProvider) {
        return protocol.decode(bytes, protocolProvider);
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
