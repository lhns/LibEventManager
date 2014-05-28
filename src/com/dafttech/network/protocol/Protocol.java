package com.dafttech.network.protocol;

import java.io.IOException;

import com.dafttech.network.Client;
import com.dafttech.network.INetworkInterface;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;

public abstract class Protocol<Packet extends IPacket> implements INetworkInterface {
    private INetworkInterface netInterface;

    public Protocol() {
    }

    public final void setNetworkInterface(INetworkInterface netInterface) {
        this.netInterface = netInterface;
    }

    public abstract Packet receive_(Client client) throws IOException;

    public abstract void send_(Packet packet) throws IOException;

    public abstract void connect();

    public abstract void disconnect(Disconnect reason);

    @SuppressWarnings("unchecked")
    public Protocol<Packet> clone(INetworkInterface netInterface) {
        Protocol<Packet> newInstance = null;
        try {
            newInstance = (Protocol<Packet>) getClass().newInstance();
            newInstance.setNetworkInterface(netInterface);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return newInstance;
    }

    public void receive(Packet packet) {
    }

    public final void send(Packet packet) {
        try {
            send_(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public final void receive__(Client client) throws IOException {
        receive(receive_(client));
    }

    public final int read() throws IOException {
        return netInterface.read();
    }

    public final byte[] read(byte[] array) throws IOException {
        return netInterface.read(array);
    }

    public final void write(byte... data) throws IOException {
        netInterface.write(data);
    }
}
