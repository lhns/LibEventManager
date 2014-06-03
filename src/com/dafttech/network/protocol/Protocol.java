package com.dafttech.network.protocol;

import com.dafttech.network.NetworkInterface;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;

public abstract class Protocol<Packet extends IPacket> extends NetworkInterface<Packet> {
    private NetworkInterface<Packet> netInterface;

    public Protocol(NetworkInterface<Packet> netInterface) {
        super(null);
        this.netInterface = netInterface;
    }

    public abstract Packet receive();

    @Override
    public abstract void send(Packet packet);

    @Override
    public void connect() {
        netInterface.connect();
    }

    @Override
    public void disconnect(Disconnect reason) {
        netInterface.disconnect(reason);
    }

    @Override
    public final void receive(Packet packet) {
    }

    @Override
    public final int available() {
        return netInterface.available();
    }

    @Override
    public final int read() {
        return netInterface.read();
    }

    @Override
    public final byte[] read(byte[] array) {
        return netInterface.read(array);
    }

    @Override
    public final void write(byte... data) {
        netInterface.write(data);
    }

    @Override
    public final boolean isAlive() {
        return true;
    }

    @Override
    public final void close() {
    }
}
