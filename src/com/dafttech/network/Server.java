package com.dafttech.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import com.dafttech.network.Client.Disconnect;

public class Server {
    private volatile ServerSocket serverSocket;
    private volatile List<Client> clients = new LinkedList<Client>();
    private ServerThread thread;

    public Server(int port) throws IOException {
        this(new ServerSocket(port));
    }

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        thread = new ServerThread();
        thread.start();
    }

    public final void close() {
        for (Client client : clients)
            client.close();
        thread.closed = true;
    }

    public final boolean isAlive() {
        return !thread.closed;
    }

    public final ServerSocket getServerSocket() {
        return serverSocket;
    }

    public final List<Client> getClients() {
        return clients;
    }

    public final void send(Client client, int channel, byte... data) {
        client.send(channel, data);
    }

    public final void broadcast(int channel, byte... data) {
        for (Client client : clients)
            client.send(channel, data);
    }

    public void receive(Client client, int channel, byte[] data) {
    }

    public void connect(Client client) {
    }

    public void disconnect(Client client, Disconnect reason) {
    }

    private class ServerThread extends Thread {
        private volatile boolean closed = false;

        @Override
        public void run() {
            while (!closed) {
                for (int i = clients.size() - 1; i >= 0; i--)
                    if (!clients.get(i).isAlive()) clients.remove(i);
                try {
                    clients.add(new Client(serverSocket.accept()) {
                        @Override
                        public void receive(int channel, byte[] data) {
                            Server.this.receive(this, channel, data);
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
}
