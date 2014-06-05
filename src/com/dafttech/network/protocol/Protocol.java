package com.dafttech.network.protocol;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.IPacket;

public abstract class Protocol<Packet extends IPacket> extends NetworkInterface<Packet> {

    public Protocol(NetworkInterface<Packet> parent) {
        super(null, parent);
    }

    public abstract Packet receive();

    @Override
    public abstract void send(Packet packet);

    @Override
    public final void receive(Packet packet) {
    }

    @Override
    public final boolean isAlive() {
        return true;
    }

    @Override
    public final void close() {
    }
}
