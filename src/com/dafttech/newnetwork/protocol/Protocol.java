package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.NetworkInterface;
import com.dafttech.newnetwork.packet.Packet;

public abstract class Protocol<P extends Packet> {

    public abstract byte[] encode(P packet, NetworkInterface<P> netInterface);

    public abstract P decode(byte[] bytes, NetworkInterface<P> netInterface);

    public abstract Protocol<P> newInstance();
}