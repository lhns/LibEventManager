package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class ProtocolProvider<P extends Packet> {
    protected final Protocol<P> protocol;

    public ProtocolProvider(Protocol<P> protocol) {
        this.protocol = protocol;
    }

    public Protocol<P> getProtocol() {
        return protocol;
    }
}
