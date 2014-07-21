package com.dafttech.network;

import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import com.dafttech.filterlist.Filterlist;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public abstract class NetworkInterface<Packet extends IPacket> {
    private volatile Class<? extends Protocol<Packet>> protocolClass;
    private volatile NetworkInterface<Packet> parent;
    private volatile Protocol<Packet> protocol;

    public NetworkInterface(Class<? extends Protocol<Packet>> protocolClass, NetworkInterface<Packet> parent) {
        this.protocolClass = protocolClass;
        if (this.protocolClass != null) protocol = newProtocolInstance(protocolClass);
        this.parent = parent;
    }

    public abstract boolean isServer();

    public abstract boolean isClient();

    public abstract boolean isProtocol();

    public abstract boolean isAlive();

    public abstract void close();

    public Socket getSocket() {
        if (parent != null) return parent.getSocket();
        return null;
    }

    public ServerSocket getServerSocket() {
        if (parent != null) return parent.getServerSocket();
        return null;
    }

    public void connect() {
        if (parent != null) {
            if (isClient()) {
                parent.connect((Client<Packet>) this);
            } else {
                parent.connect();
            }
        }
    }

    public abstract void connect(Client<Packet> client);

    public void disconnect(Disconnect reason) {
        if (parent != null) {
            if (isClient()) {
                parent.disconnect((Client<Packet>) this, reason);
            } else {
                parent.disconnect(reason);
            }
        }
    }

    public abstract void disconnect(Client<Packet> client, Disconnect reason);

    public void receive(Packet packet) {
        if (parent != null && packet != null) {
            if (isClient()) {
                parent.receive((Client<Packet>) this, packet);
            } else {
                parent.receive(packet);
            }
        }
    }

    public abstract void receive(Client<Packet> client, Packet packet);

    public void send(Packet packet) {
        if (protocol != null && packet != null) protocol.send(packet);
    }

    public abstract void send(Filterlist<Client<?>> clientFilter, Packet packet);

    public int available() {
        if (parent != null) return parent.available();
        return 0;
    }

    public int read() {
        if (parent != null) return parent.read();
        return 0;
    }

    public byte[] read(byte[] array) {
        if (parent != null && array != null) return parent.read(array);
        return null;
    }

    public void write(byte... data) {
        if (parent != null && data != null) parent.write(data);
    }

    protected void removeClient(NetworkInterface<Packet> client) {

    }

    public Class<? extends Protocol<Packet>> getProtocolClass() {
        return protocolClass;
    }

    public Protocol<Packet> getProtocol() {
        return protocol;
    }

    public NetworkInterface<Packet> getParent() {
        return parent;
    }

    private Protocol<Packet> newProtocolInstance(Class<? extends Protocol<Packet>> protocolClass) {
        try {
            return protocolClass.getConstructor(NetworkInterface.class).newInstance(this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }
}
