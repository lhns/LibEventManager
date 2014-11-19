package com.dafttech.network.protocol;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.SimplePacket;

import java.nio.ByteBuffer;

import static com.dafttech.primitive.PrimitiveUtil.INTEGER;

public class SimpleProtocol extends Protocol<SimplePacket> {
    public SimpleProtocol(NetworkInterface<SimplePacket> netInterface) {
        super(netInterface);
    }

    @Override
    public SimplePacket receive() {
        byte[] integer = new byte[4];
        return new SimplePacket(INTEGER.fromByteArray2(read(integer)), read(new byte[INTEGER.fromByteArray2(read(integer))]));
    }

    @Override
    public void send(SimplePacket packet) {
        ByteBuffer packetBuffer = ByteBuffer.allocate(8 + packet.data.length);
        packetBuffer.put(INTEGER.toByteArray2(packet.channel));
        packetBuffer.put(INTEGER.toByteArray2(packet.data.length));
        packetBuffer.put(packet.data);
        write(packetBuffer.array());
    }
}
