package com.dafttech.network;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.disconnect.Quit;
import com.dafttech.network.disconnect.Unknown;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public class Client<Packet extends IPacket> extends NetworkInterface<Packet> {
    private volatile Socket socket;
    private ClientThread thread;

    protected Client(Class<? extends Protocol<Packet>> protocolClass, Socket socket) {
        super(protocolClass);
        this.socket = socket;
        thread = new ClientThread();
        thread.start();
    }

    public Client(Class<? extends Protocol<Packet>> protocolClass, InetAddress address, int port) throws IOException {
        this(protocolClass, new Socket(address, port));
    }

    public Client(Class<? extends Protocol<Packet>> protocolClass, String host, int port) throws UnknownHostException,
            IOException {
        this(protocolClass, InetAddress.getByName(host), port);
    }

    public Client(Class<? extends Protocol<Packet>> protocolClass, String host, String port) throws UnknownHostException,
            IOException {
        this(protocolClass, host, Integer.valueOf(port));
    }

    public Client(Class<? extends Protocol<Packet>> protocolClass, String host) throws UnknownHostException, IOException {
        this(protocolClass, host.split(":")[0], host.split(":")[1]);
    }

    public final Socket getSocket() {
        return socket;
    }

    @Override
    public final boolean isAlive() {
        return thread.reason == null;
    }

    @Override
    public final void close() {
        close(new Quit());
    }

    protected final void close(Disconnect reason) {
        thread.reason = reason;
    }

    private class ClientThread extends Thread {
        private volatile Disconnect reason = null;

        @Override
        public void run() {
            getProtocol().connect();

            while (Client.this.isAlive()) {
                Packet packet = getProtocol().receive();
                if (packet != null) receive(packet);
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            getProtocol().disconnect(reason);
        }
    }

    @Override
    public final int available() {
        if (isAlive()) {
            try {
                return socket.getInputStream().available();
            } catch (IOException e) {
                processException(e);
            }
        }
        return 0;
    }

    @Override
    public final int read() {
        if (isAlive()) {
            try {
                int result = socket.getInputStream().read();
                if (result == -1) throw new EOFException();
                return result;
            } catch (IOException e) {
                processException(e);
            }
        }
        return 0;
    }

    @Override
    public final byte[] read(byte[] array) {
        if (array.length > 0 && isAlive()) {
            try {
                int bytesLeft = array.length, bytesRead;
                ByteBuffer inputBuffer = ByteBuffer.allocate(bytesLeft);
                byte[] readArray;
                while (bytesLeft > 0) {
                    readArray = new byte[bytesLeft];
                    bytesRead = socket.getInputStream().read(readArray);
                    if (bytesRead == -1) throw new EOFException();
                    inputBuffer.put(readArray, 0, bytesRead);
                    bytesLeft -= bytesRead;
                }
                array = inputBuffer.array();
            } catch (IOException e) {
                processException(e);
            }
        }
        return array;
    }

    @Override
    public final void write(byte... data) {
        if (isAlive()) {
            try {
                socket.getOutputStream().write(data);
                socket.getOutputStream().flush();
            } catch (IOException e) {
                processException(e);
            }
        }
    }

    @Override
    public void connect() {
    }

    @Override
    public void disconnect(Disconnect reason) {
    }

    @Override
    public void receive(Packet packet) {
    }

    @Override
    public final void send(Packet packet) {
        getProtocol().send(packet);
    }

    private void processException(Exception e) {
        Disconnect reason = Disconnect.fromException(e);
        close(reason);
        if (reason instanceof Unknown) e.printStackTrace();
    }
}
