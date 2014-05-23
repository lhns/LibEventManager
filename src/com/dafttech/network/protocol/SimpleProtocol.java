package com.dafttech.network.protocol;

import java.io.IOException;

import com.dafttech.network.packet.SimplePacket;

public class SimpleProtocol extends Protocol<SimplePacket> {
    @Override
    public final SimplePacket receive_() throws IOException {
        byte[] integer = new byte[4], data;
        int channel, size;
        read(integer);
        channel = fromByteArray(integer);
        read(integer);
        size = fromByteArray(integer);
        data = new byte[size];
        read(data);
        return new SimplePacket(channel, data);
    }

    @Override
    public final void send_(SimplePacket packet) throws IOException {
        // TODO Auto-generated method stub

    }
}
