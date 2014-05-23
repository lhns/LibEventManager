package com.dafttech.network.packet;

public class SimplePacket implements IPacket {
    public int channel;
    public byte[] data;

    public SimplePacket(int channel, byte[] data) {
        this.channel = channel;
        this.data = data;
    }
}
