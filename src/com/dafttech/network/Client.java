package com.dafttech.network;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import com.dafttech.filterlist.Filterlist;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.disconnect.Quit;
import com.dafttech.network.disconnect.Unknown;
import com.dafttech.network.packet.IPacket;
import com.dafttech.network.protocol.Protocol;

public class Client<Packet extends IPacket> extends NetworkInterface<Packet> {
    private volatile Socket socket;
    private ClientThread thread;

    protected Client(Class<? extends Protocol<Packet>> protocolClass, Socket socket, NetworkInterface<Packet> parent) {
        super(protocolClass, parent);
        this.socket = socket;
        thread = new ClientThread();
        thread.start();
    }

    public Client(Class<? extends Protocol<Packet>> protocolClass, Socket socket) {
        this(protocolClass, socket, null);
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

    @Override
    public final Socket getSocket() {
        return socket;
    }

    @Override
    public boolean isServer() {
        return false;
    }

    @Override
    public boolean isClient() {
        return true;
    }

    @Override
    public boolean isProtocol() {
        return false;
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

            if (getParent() != null) getParent().removeClient(Client.this);

            getProtocol().disconnect(reason);
        }
    }

    @Override
    public final int available() {
        if (!isAlive()) return 0;
        try {
            return socket.getInputStream().available();
        } catch (IOException e) {
            processException(e);
        }
        return 0;
    }

    @Override
    public final int read() {
        if (!isAlive()) return 0;
        try {
            int result = socket.getInputStream().read();
            if (result == -1) throw new EOFException();
            return result;
        } catch (IOException e) {
            processException(e);
        }
        return 0;
    }

    @Override
    public final byte[] read(byte[] array) {
        if (!isAlive()) return array;
        if (array.length == 0) return array;
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
        return array;
    }

    @Override
    public final void write(byte... data) {
        if (!isAlive()) return;
        try {
            socket.getOutputStream().write(data);
            socket.getOutputStream().flush();
        } catch (IOException e) {
            processException(e);
        }
    }

    private void processException(Exception e) {
        Disconnect reason = Disconnect.fromException(e);
        close(reason);
        if (reason instanceof Unknown) e.printStackTrace();
    }

    @Override
    public final void connect(Client<Packet> client) {
    }

    @Override
    public final void disconnect(Client<Packet> client, Disconnect reason) {
    }

    @Override
    public final void receive(Client<Packet> client, Packet packet) {
    }

    @Override
    public final void send(Filterlist<Client<?>> clientFilter, Packet packet) {
        if (clientFilter.isFiltered(this)) send(packet);
    }
}
