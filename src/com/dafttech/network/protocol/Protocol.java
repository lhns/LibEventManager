package com.dafttech.network.protocol;

import java.io.EOFException;
import java.io.IOException;

import com.dafttech.network.Client;
import com.dafttech.network.packet.IPacket;

public abstract class Protocol<Packet extends IPacket> {
    private Client client;

    public Protocol() {
    }

    public final void setClient(Client client) {
        this.client = client;
    }

    public final void receive__() throws IOException {
        receive(receive_());
    }

    public abstract Packet receive_() throws IOException;

    public void receive(Packet packet) {
    }

    public final void send(Packet packet) {
        try {
            send_(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void send_(Packet packet) throws IOException;

    public void connect() {
    }

    public void disconnect(Client.Disconnect reason) {
    }

    public final int read() throws IOException {
        if (client.isAlive()) {
            int result = client.getInputStream().read();
            if (result == -1) throw new EOFException();
            return result;
        }
        return 0;
    }

    public final void read(byte[] array) throws IOException {
        if (client.isAlive()) {
            int result = client.getInputStream().read(array);
            if (result == -1) throw new EOFException();
        }
    }

    public final void write(byte... data) {
        if (client.isAlive()) {
            try {
                client.getOutputStream().write(data);
                client.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
