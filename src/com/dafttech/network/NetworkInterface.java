package com.dafttech.network;

import java.lang.reflect.InvocationTargetException;

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public abstract class NetworkInterface<Packet extends IPacket> {
    private Class<? extends Protocol<Packet>> protocolClass;
    private Protocol<Packet> protocol;

    public NetworkInterface(Class<? extends Protocol<Packet>> protocolClass) {
        if (protocolClass != null) {
            this.protocolClass = protocolClass;
            protocol = newProtocolInstance(protocolClass);
        }
    }

    public abstract boolean isAlive();

    public abstract void close();

    public abstract void connect();

    public abstract void disconnect(Disconnect reason);

    public abstract void receive(Packet packet);

    public abstract void send(Packet packet);

    public abstract int available();

    public abstract int read();

    public abstract byte[] read(byte[] array);

    public abstract void write(byte... data);

    public Class<? extends Protocol<Packet>> getProtocolClass() {
        return protocolClass;
    }

    public Protocol<Packet> getProtocol() {
        return protocol;
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
