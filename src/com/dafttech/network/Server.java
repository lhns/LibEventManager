package com.dafttech.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private List<Client> clients = new LinkedList<Client>();
    private Thread creator;
    private boolean closed;

    public Server() {
        creator = Thread.currentThread();
        closed = false;
    }

    @Override
    public void run() {
        while (!closed && creator.isAlive()) {
            try {
                clients.add(new Client(serverSocket.accept()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        closed = true;
    }
}
