package com.dafttech.network.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.packet.RawPacket;

public class RawProtocol extends Protocol<RawPacket> {

    public RawProtocol(NetworkInterface<RawPacket> netInterface) {
        super(netInterface);
    }

    @Override
    public RawPacket receive() throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byteStream.write(read());
        byteStream.write(read(new byte[available()]));
        return new RawPacket(byteStream.toByteArray());
    }

    @Override
    public void send(RawPacket packet) throws IOException {
        write(packet.data);
    }

}
