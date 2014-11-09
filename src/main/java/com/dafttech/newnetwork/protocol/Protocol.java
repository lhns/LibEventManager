package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class Protocol<P extends Packet> {
    protected abstract byte[] encode(P packet, ProtocolProvider<P> protocolProvider);

    protected abstract P decode(byte[] bytes, ProtocolProvider<P> protocolProvider);

    public void close() {
    }
}