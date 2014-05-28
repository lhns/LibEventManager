package com.dafttech.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public class Server extends Protocol {
    private volatile ServerSocket serverSocket;
    private volatile List<Client> clients = new LinkedList<Client>();
    protected volatile Protocol<?> protocol;
    private ServerThread thread;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
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
        this.protocol = protocol;
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
    public IPacket receive_() throws IOException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void send_(IPacket packet) throws IOException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void connect() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void disconnect(Disconnect reason) {
        // TODO Auto-generated method stub
        
    }
}
