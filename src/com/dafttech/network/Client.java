package com.dafttech.network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class Client {
    public static enum Disconnect {
        NOSTREAM, EOF, RESET, TIMEOUT, UNKNOWN, QUIT
    };

    private volatile Socket socket;
    private volatile InputStream inputStream;
    private volatile OutputStream outputStream;
    private ClientThread thread;

    public Client(Socket socket) {
        this.socket = socket;
        thread = new ClientThread();
        thread.start();
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
        close(Disconnect.QUIT);
    }

    public final void close(Disconnect reason) {
        thread.closed = true;
        thread.reason = reason;
    }

    public final boolean isAlive() {
        return !thread.closed;
    }

    public final Socket getSocket() {
        return socket;
    }

    public final InputStream getInputStream() {
        return inputStream;
    }

    public final OutputStream getOutputStream() {
        return outputStream;
    }

    public final int readRaw() throws IOException {
        int result = inputStream.read();
        if (result == -1) throw new EOFException();
        return result;
    }

    public final void readRaw(byte[] array) throws IOException {
        int result = inputStream.read(array);
        if (result == -1) throw new EOFException();
    }

    public final void sendRaw(byte... data) {
        if (isAlive()) {
            try {
                outputStream.write(data);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public final void send(int channel, byte... data) {
        ByteBuffer packet = ByteBuffer.allocate(8 + data.length);
        packet.put(toByteArray(channel));
        packet.put(toByteArray(data.length));
        packet.put(data);
        sendRaw(packet.array());
    }

    public void receiveRaw() throws IOException {
        byte[] integer = new byte[4], data;
        int channel, size;
        readRaw(integer);
        channel = fromByteArray(integer);
        readRaw(integer);
        size = fromByteArray(integer);
        data = new byte[size];
        readRaw(data);
        receive(channel, data);
    }

    public void receive(int channel, byte[] data) {
    }

    public void connect() {
    }

    public void disconnect(Disconnect reason) {
    }

    private class ClientThread extends Thread {
        private volatile boolean closed = false;
        private volatile Disconnect reason = null;

        @Override
        public void run() {
            connect();

            try {
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                close(Disconnect.NOSTREAM);
            }

            while (Client.this.isAlive()) {
                try {
                    receiveRaw();
                } catch (IOException e) {
                    if (e instanceof EOFException) {
                        close(Disconnect.EOF);
                    } else if (e instanceof SocketException && e.getMessage().equals("Connection reset")) {
                        close(Disconnect.RESET);
                    } else if (e instanceof SocketTimeoutException) {
                        close(Disconnect.TIMEOUT);
                    } else {
                        close(Disconnect.UNKNOWN);
                        e.printStackTrace();
                    }
                }
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            disconnect(reason);
        }
    }

    public static byte[] toByteArray(int value) {
        return new byte[] { (byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value };
    }

    public static int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | bytes[3] & 0xFF;
    }
}
