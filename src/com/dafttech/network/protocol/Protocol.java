package com.dafttech.network.protocol;

import com.dafttech.filterlist.Filterlist;
import com.dafttech.network.Client;
import com.dafttech.network.NetworkInterface;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;

public abstract class Protocol<Packet extends IPacket> extends NetworkInterface<Packet> {

    public Protocol(NetworkInterface<Packet> parent) {
        super(null, parent);
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public boolean isProtocol() {
        return true;
    }

    public abstract Packet receive();

    @Override
    public abstract void send(Packet packet);

    @Override
    public final void receive(Packet packet) {
    }

    @Override
    public final boolean isAlive() {
        return true;
    }

    @Override
    public final void close() {
    }

    @Override
    public final void connect(Client<Packet> client) {
    }

    @Override
    public final void disconnect(Client<Packet> client, Disconnect reason) {
    }

    @Override
    public final void receive(Client<Packet> client, Packet packet) {
    }

    @Override
    public final void send(Filterlist<Client<?>> clientFilter, Packet packet) {
        if (clientFilter.isFiltered(getParent())) send(packet);
    }
}
