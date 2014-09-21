package com.dafttech.network.protocol;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.RawPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RawProtocol extends Protocol<RawPacket> {

    public RawProtocol(NetworkInterface<RawPacket> netInterface) {
        super(netInterface);
    }

    @Override
    public RawPacket receive() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(read());
        try {
            byteStream.write(read(new byte[available()]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RawPacket(byteStream.toByteArray());
    }

    @Override
    public void send(RawPacket packet) {
        write(packet.data);
    }

}
