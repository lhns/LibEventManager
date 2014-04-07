package com.dafttech.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class Client {
    public static enum Disconnect {
        QUIT, TIMEOUT, UNKNOWN
    };

    private volatile Socket socket;
    private ClientThread thread;

    public Client(Socket socket) {
        this.socket = socket;
        reconnect();
    }

    public Client(InetAddress address, int port) throws IOException {
        this(new Socket(address, port));
    }

    public Client(String host, int port) throws UnknownHostException, IOException {
        this(InetAddress.getByName(host), port);
    }

    public Client(String host) throws UnknownHostException, IOException {
        this(host.split(":")[0], Integer.valueOf(host.split(":")[1]));
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

    public final void reconnect() {
        if (thread != null && isAlive()) close();
        thread = new ClientThread();
        thread.start();
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

    public void connect() {
    }

    public void disconnect(Disconnect reason) {
    }

    private class ClientThread extends Thread {
        private volatile boolean closed = false;

        @Override
        public void run() {
            connect();
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
                    close();
                    Disconnect reason;
                    if (e instanceof SocketException && e.getMessage().equals("Connection reset")) {
                        reason = Disconnect.QUIT;
                    } else if (e instanceof SocketTimeoutException) {
                        reason = Disconnect.TIMEOUT;
                    } else {
                        reason = Disconnect.UNKNOWN;
                    }
                    disconnect(reason);
                    if (reason == Disconnect.UNKNOWN) e.printStackTrace();
                }
            }
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
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
