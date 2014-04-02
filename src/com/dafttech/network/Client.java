package com.dafttech.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client {
    private volatile Socket socket;
    private ClientThread thread;

    public Client(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getByName(host), port);
    }

    public Client(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public Client(Socket socket) {
        this.socket = socket;
        thread = new ClientThread();
        thread.start();
    }

    public final void close() {
        thread.closed = true;
    }

    public final boolean isAlive() {
        return !thread.closed;
    }

    public final Socket getSocket() {
        return socket;
    }

    public final InputStream getInputStream() {
        try {
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final OutputStream getOutputStream() {
        try {
            return socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final void send(int channel, byte... data) {
        if (isAlive()) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(toByteArray(channel));
                outputStream.write(toByteArray(data.length));
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receive(int channel, byte[] data) {
    }

    private class ClientThread extends Thread {
        private volatile boolean closed;

        @Override
        public void run() {
            while (!closed) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] integer = new byte[4];
                    inputStream.read(integer);
                    int channel = fromByteArray(integer);
                    inputStream.read(integer);
                    byte[] data = new byte[fromByteArray(integer)];
                    inputStream.read(data);
                    receive(channel, data);
                } catch (IOException e) {
                    if (e instanceof SocketException) {
                        closed = true;
                        return;
                    }
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] toByteArray(int value) {
        return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
    }

    private static int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | bytes[3] & 0xFF;
    }
}
