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

import com.dafttech.network.protocol.Protocol;

public class Client {
    public static enum Disconnect {
        NOSTREAM, EOF, RESET, TIMEOUT, UNKNOWN, QUIT
    };

    private volatile Socket socket;
    private volatile InputStream inputStream;
    private volatile OutputStream outputStream;
    private ClientThread thread;
    private Protocol<?> protocol;

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

    public Client(String host, String port) throws UnknownHostException, IOException {
        this(host, Integer.valueOf(port));
    }

    public Client(String host) throws UnknownHostException, IOException {
        this(host.split(":")[0], host.split(":")[1]);
    }

    public final void setProtocol(Protocol<?> protocol) {
        this.protocol = protocol;
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
                    if (protocol != null) protocol.receive__();
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
}
