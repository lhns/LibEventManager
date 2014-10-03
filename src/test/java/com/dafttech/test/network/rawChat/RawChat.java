package com.dafttech.test.network.rawChat;

import com.dafttech.network.Client;
import com.dafttech.network.NetworkInterface;
import com.dafttech.network.Server;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.RawPacket;
import com.dafttech.network.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RawChat {
    public static NetworkInterface<RawPacket> net = null;

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server? ");
        boolean isServer = Boolean.valueOf(input.readLine());
        System.out.println(isServer);
        System.out.println("Address/Port: ");
        String address = input.readLine();
        if (isServer) {
            try {
                net = new Server<RawPacket>(RawProtocol.class, address) {

                    @Override
                    public void receive(Client<RawPacket> client, RawPacket packet) {
                        readByteArray(packet.data);
                    }

                    ;

                    @Override
                    public void connect(Client<RawPacket> client) {
                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Connection Requested");
                    }

                    @Override
                    public void disconnect(Client<RawPacket> client, Disconnect reason) {
                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Disconnect "
                                + reason.toString());
                    }
                };
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(net.getServerSocket().getLocalSocketAddress().toString());
            while (true)
                net.send(new RawPacket(sendByteArray(input)));
        } else {
            net = new Client<RawPacket>(RawProtocol.class, address) {
                @Override
                public void receive(RawPacket packet) {
                    readByteArray(packet.data);
                }

                ;

                @Override
                public void connect() {
                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Connection Requested");
                }

                @Override
                public void disconnect(Disconnect reason) {
                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
                }
            };
            System.out.println(net.getSocket().getLocalSocketAddress().toString());
            while (true)
                net.send(new RawPacket(sendByteArray(input)));
        }
    }

    public static String recv = "";

    public static void readByteArray(byte[] data) {
        recv = recv + new String(data);
        if (!recv.equals("")) {
            String outString = "b";
            for (byte b : recv.getBytes())
                outString = outString + " " + b;
            outString = outString + "\ns " + recv;
            System.out.println(outString);
            recv = "";
        }
    }

    public static byte[] sendByteArray(BufferedReader input) throws IOException {
        String in = input.readLine();
        if (in.startsWith("b ")) {
            in = in.substring(2);
            String[] bytes = in.split(" ");
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            List<ByteArrayOutputStream> repeat = new ArrayList<ByteArrayOutputStream>();
            for (int i = 0; i < bytes.length; i++) {
                String currChar = bytes[i];
                if (currChar.equals("r")) {
                    repeat.add(new ByteArrayOutputStream());
                } else if (repeat.size() > 0) {
                    if (currChar.equals("/r")) {
                        byte[] tmpArray = repeat.get(repeat.size() - 1).toByteArray();
                        repeat.get(repeat.size() - 1).close();
                        repeat.remove(repeat.size() - 1);
                        ByteArrayOutputStream repeatStream = outStream;
                        if (repeat.size() > 0) repeatStream = repeat.get(repeat.size() - 1);
                        for (int i1 = 0; i1 < Integer.valueOf(bytes[i + 1]); i1++)
                            repeatStream.write(tmpArray);
                        i++;
                    } else {
                        repeat.get(repeat.size() - 1).write(Integer.valueOf(currChar));
                    }
                } else {
                    outStream.write(Integer.valueOf(currChar));
                }
            }
            byte[] outArray = outStream.toByteArray();
            outStream.close();
            return outArray;
        } else if (in.startsWith("s ")) {
            in = in.substring(2);
            return in.getBytes();
        }
        return null;
    }

}