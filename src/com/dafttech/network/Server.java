package com.dafttech.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import com.dafttech.filterlist.Filterlist;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public class Server<Packet extends IPacket> extends NetworkInterface<Packet> {
    private volatile ServerSocket serverSocket;
    private volatile List<Client<Packet>> clients = new LinkedList<Client<Packet>>();
    private ServerThread thread;

    public Server(Class<? extends Protocol<Packet>> protocolClass, ServerSocket serverSocket) {
        super(protocolClass);
        this.serverSocket = serverSocket;
        thread = new ServerThread();
        thread.start();
    }

    public Server(Class<? extends Protocol<Packet>> protocolClass, int port) throws IOException {
        this(protocolClass, new ServerSocket(port));
    }

    public Server(Class<? extends Protocol<Packet>> protocolClass, String port) throws IOException {
        this(protocolClass, Integer.valueOf(port));
    }

    public final ServerSocket getServerSocket() {
        return serverSocket;
    }

    public final List<Client<Packet>> getClients() {
        return clients;
    }

    @Override
    public final boolean isAlive() {
        return !thread.closed;
    }

    @Override
    public final void close() {
        for (Client<Packet> client : clients)
            client.close();
        thread.closed = true;
    }

    private class ServerThread extends Thread {
        private volatile boolean closed = false;

        @Override
        public void run() {
            while (!closed) {
                for (int i = clients.size() - 1; i >= 0; i--)
                    if (!clients.get(i).isAlive()) clients.remove(i);
                try {
                    clients.add(new Client<Packet>(getProtocolClass(), serverSocket.accept()) {
                        @Override
                        public void receive(Packet packet) {
                            Server.this.receive(this, packet);
                        }

                        @Override
                        public void connect() {
                            Server.this.connect(this);
                        }

                        @Override
                        public void disconnect(Disconnect reason) {
                            Server.this.disconnect(this, reason);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public final int available() {
        return 0;
    }

    @Override
    public final int read() {
        return 0;
    }

    @Override
    public final byte[] read(byte[] array) {
        return array;
    }

    @Override
    public final void write(byte... data) {
        for (Client<Packet> client : clients)
            client.write(data);
    }

    @Override
    public final void connect() {
    }

    @Override
    public final void disconnect(Disconnect reason) {
    }

    public void connect(Client<Packet> client) {
    }

    public void disconnect(Client<Packet> client, Disconnect reason) {
    }

    @Override
    public final void receive(Packet packet) {
    }

    public void receive(Client<Packet> client, Packet packet) {
    }

    @Override
    public final void send(Packet packet) {
        for (Client<Packet> client : clients)
            client.send(packet);
    }

    public final void send(Filterlist<Client<Packet>> clientFilter, Packet packet) {
        for (Client<Packet> client : clients)
            if (clientFilter.isFiltered(client)) client.send(packet);
    }
}
