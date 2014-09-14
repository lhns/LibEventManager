package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

public abstract class Protocol<P extends Packet> {

    public abstract byte[] encode(P packet, ProtocolProvider<P> netInterface);

    public abstract P decode(byte[] bytes, ProtocolProvider<P> netInterface);

    public abstract Protocol<P> newInstance();
}