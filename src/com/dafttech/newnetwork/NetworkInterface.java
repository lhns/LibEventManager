package com.dafttech.newnetwork;

import com.dafttech.newnetwork.packet.Packet;
import com.dafttech.newnetwork.protocol.Protocol;

public abstract class NetworkInterface<P extends Packet> {
    protected final Protocol<P> protocol;

    public NetworkInterface(Protocol<P> protocol) {
        this.protocol = protocol;
    }

    public Protocol<P> getProtocol() {
        return protocol;
    }
}
