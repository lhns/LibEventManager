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

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.disconnect.EOF;
import com.dafttech.network.disconnect.Quit;
import com.dafttech.network.disconnect.Reset;
import com.dafttech.network.disconnect.Timeout;
import com.dafttech.network.disconnect.Unknown;
import com.dafttech.network.protocol.Protocol;

public class Client {
    private volatile Socket socket;
    private volatile Server server;
    private volatile Protocol<?> protocol;
    private ClientThread thread;

    protected Client(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        thread = new ClientThread();
        thread.start();
    }

    protected Client(Socket socket) {
        this(socket, null);
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

    public final Socket getSocket() {
        return socket;
    }

    public final Server getServer() {
        return server;
    }

    public final Protocol<?> getProtocol() {
        if (server != null && server.protocol != null) {
            return server.protocol;
        } else if (protocol != null) {
            return protocol;
        } else {
            return null;
        }
    }

    public final InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public final OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public final void setProtocol(Protocol<?> protocol) {
        this.protocol = protocol;
    }

    public final void close() {
        close(new Quit());
    }

    public final void close(Disconnect reason) {
        thread.reason = reason;
    }

    public final boolean isAlive() {
        return thread.reason == null;
    }

    private class ClientThread extends Thread {
        private volatile Disconnect reason = null;

        @Override
        public void run() {
            getProtocol().connect();

            while (Client.this.isAlive()) {
                try {
                    getProtocol().receive__();
                } catch (IOException e) {
                    if (e instanceof EOFException) {
                        close(new EOF());
                    } else if (e instanceof SocketException && e.getMessage().equals("Connection reset")) {
                        close(new Reset());
                    } else if (e instanceof SocketTimeoutException) {
                        close(new Timeout());
                    } else {
                        close(new Unknown());
                        e.printStackTrace();
                    }
                }
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getProtocol().disconnect(reason);
        }
    }
}
