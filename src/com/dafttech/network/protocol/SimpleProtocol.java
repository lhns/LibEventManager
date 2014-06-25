package com.dafttech.network.protocol;

import java.nio.ByteBuffer;

import com.dafttech.manager.PrimitiveManager;
import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.SimplePacket;

public class SimpleProtocol extends Protocol<SimplePacket> {
    public SimpleProtocol(NetworkInterface<SimplePacket> netInterface) {
        super(netInterface);
    }

    @Override
    public SimplePacket receive() {
        // System.out.println(getParent());
        byte[] integer = new byte[4];
        return new SimplePacket(PrimitiveManager.INTEGER.fromByteArray(read(integer)),
                read(new byte[PrimitiveManager.INTEGER.fromByteArray(read(integer))]));
    }

    @Override
    public void send(SimplePacket packet) {
        ByteBuffer packetBuffer = ByteBuffer.allocate(8 + packet.data.length);
        packetBuffer.put(PrimitiveManager.INTEGER.toByteArray(packet.channel));
        packetBuffer.put(PrimitiveManager.INTEGER.toByteArray(packet.data.length));
        packetBuffer.put(packet.data);
        write(packetBuffer.array());
    }
}
