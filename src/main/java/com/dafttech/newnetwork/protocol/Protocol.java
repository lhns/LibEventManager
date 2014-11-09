package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.Packet;

import java.util.function.Consumer;

public abstract class Protocol<P extends Packet> {
    protected ProtocolProvider<P> protocolProvider = null;

    protected abstract void encode(P packet, Consumer<byte[]> submitBytes);

    protected abstract void decode(byte[] bytes, Consumer<P> submitPacket);

    public void close() {
    }
}