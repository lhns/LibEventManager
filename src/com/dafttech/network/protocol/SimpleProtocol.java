package com.dafttech.network.protocol;

import java.nio.ByteBuffer;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.SimplePacket;
import com.dafttech.type.Type;

public class SimpleProtocol extends Protocol<SimplePacket> {
    public SimpleProtocol(NetworkInterface<SimplePacket> netInterface) {
        super(netInterface);
    }

    @Override
    public SimplePacket receive() {
        // System.out.println(getParent());
        byte[] integer = new byte[4];
        return new SimplePacket(Type.INTEGER.fromByteArray(read(integer)).getValue(), read(new byte[Type.INTEGER.fromByteArray(
                read(integer)).getValue()]));
    }

    @Override
    public void send(SimplePacket packet) {
        ByteBuffer packetBuffer = ByteBuffer.allocate(8 + packet.data.length);
        packetBuffer.put(Type.INTEGER.create(packet.channel).toByteArray());
        packetBuffer.put(Type.INTEGER.create(packet.data.length).toByteArray());
        packetBuffer.put(packet.data);
        write(packetBuffer.array());
    }
}
