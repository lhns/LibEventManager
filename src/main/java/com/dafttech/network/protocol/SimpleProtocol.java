package com.dafttech.network.protocol;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.SimplePacket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.dafttech.primitive.PrimitiveUtil.INTEGER;

public class SimpleProtocol extends Protocol<SimplePacket> {
    public SimpleProtocol(NetworkInterface<SimplePacket> netInterface) {
        super(netInterface);
    }

    @Override
    public SimplePacket receive() {
        byte[] integer = new byte[4];
        return new SimplePacket(INTEGER.fromByteArray(read(integer), ByteOrder.BIG_ENDIAN), read(new byte[INTEGER.fromByteArray(read(integer), ByteOrder.BIG_ENDIAN)]));
    }

    @Override
    public void send(SimplePacket packet) {
        ByteBuffer packetBuffer = ByteBuffer.allocate(8 + packet.data.length);
        packetBuffer.put(INTEGER.toByteArray(packet.channel, ByteOrder.BIG_ENDIAN));
        packetBuffer.put(INTEGER.toByteArray(packet.data.length, ByteOrder.BIG_ENDIAN));
        packetBuffer.put(packet.data);
        write(packetBuffer.array());
    }
}
