package com.dafttech.network.packet;

public class RawPacket implements IPacket {
    public byte[] data;

    public RawPacket(byte[] data) {
        this.data = data;
    }
}
