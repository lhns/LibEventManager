package com.dafttech.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public class Server implements INetworkInterface {
    private volatile ServerSocket serverSocket;
    private volatile List<Client> clients = new LinkedList<Client>();
    protected volatile Protocol<?> protocol;
    private ServerThread thread;

    public Server(ServerSocket serverSocket, Protocol<?> protocol) {
        this.serverSocket = serverSocket;
        this.protocol = protocol.clone(this);
        thread = new ServerThread();
        thread.start();
    }

    public Server(int port) throws IOException {
        this(new ServerSocket(port));
    }

    public Server(String port) throws IOException {
        this(Integer.valueOf(port));
    }

    public final ServerSocket getServerSocket() {
        return serverSocket;
    }

    public final List<Client> getClients() {
        return clients;
    }

    public final Protocol<?> getProtocol() {
        return protocol;
    }

    public final void setProtocol(Protocol<?> protocol) {
        this.protocol = protocol.clone(this);
    }

    public final void close() {
        for (Client client : clients)
            client.close();
        thread.closed = true;
    }

    public final boolean isAlive() {
        return !thread.closed;
    }

    private class ServerThread extends Thread {
        private volatile boolean closed = false;

        @Override
        public void run() {
            while (!closed) {
                for (int i = clients.size() - 1; i >= 0; i--)
                    if (!clients.get(i).isAlive()) clients.remove(i);
                try {
                    clients.add(new Client(serverSocket.accept()));
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
    public int read() throws IOException {
        return 0;
    }

    @Override
    public byte[] read(byte[] array) throws IOException {
        return array;
    }

    @Override
    public void write(byte... data) throws IOException {
        for (Client client : clients)
            client.write(data);
    }
}
