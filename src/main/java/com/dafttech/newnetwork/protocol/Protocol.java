package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class Protocol<P extends Packet> {
    protected ProtocolProvider<P> protocolProvider = null;

    protected abstract byte[] encode(P packet);

    protected abstract P decode(byte[] bytes);

    public void close() {
    }
}