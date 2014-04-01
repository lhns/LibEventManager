package com.dafttech.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;

    public Client(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getByName(host), port);
    }

    public Client(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public Client(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
