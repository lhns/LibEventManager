package com.dafttech.network;

import java.lang.reflect.InvocationTargetException;

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public abstract class NetworkInterface<Packet extends IPacket> {
    private Class<? extends Protocol<Packet>> protocolClass;
    private NetworkInterface<Packet> parent;
    private Protocol<Packet> protocol;

    public NetworkInterface(Class<? extends Protocol<Packet>> protocolClass, NetworkInterface<Packet> parent) {
        if (protocolClass != null) {
            this.protocolClass = protocolClass;
            protocol = newProtocolInstance(protocolClass);
            this.parent = parent;
        }
    }

    public abstract boolean isAlive();

    public abstract void close();

    public void connect() {
        if (parent != null) parent.connect();
    }

    public void disconnect(Disconnect reason) {
        if (parent != null) parent.disconnect(reason);
    }

    public void receive(Packet packet) {
        if (parent != null && packet != null) parent.receive(packet);
    }

    public void send(Packet packet) {
        if (protocol != null && packet != null) protocol.send(packet);
    }

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
