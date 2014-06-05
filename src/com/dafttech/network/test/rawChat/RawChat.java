package com.dafttech.network.test.rawChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dafttech.network.Client;
import com.dafttech.network.NetworkInterface;
import com.dafttech.network.Server;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.RawPacket;
import com.dafttech.network.protocol.RawProtocol;

public class RawChat {

    static boolean isServer = false;
    static NetworkInterface<RawPacket> net = null;
    static int count = 0;
    static String recv = "";

    /**
     * 
     * @param args
     * 
     * @throws IOException
     * 
     * @throws NumberFormatException
     */

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server? ");
        isServer = Boolean.valueOf(input.readLine());
        System.out.println(isServer);
        System.out.println("Address/Port: ");
        String address = input.readLine();
        if (isServer) {
            try {
                net = new Server<RawPacket>(RawProtocol.class, address) {

                    @Override
                    public void receive(Client<RawPacket> client, RawPacket packet) {
                        recv = recv + new String(packet.data);
                        if (!recv.equals("")) {
                            String outString = "b";
                            for (byte b : recv.getBytes())
                                outString = outString + " " + b;
                            outString = outString + "\ns " + recv;
                            System.out.println(outString);
                            recv = "";
                        }
                    };

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
                net.send(new RawPacket(input.readLine().getBytes()));
        } else {
            net = new Client<RawPacket>(RawProtocol.class, address) {
                @Override
                public void receive(RawPacket packet) {
                    recv = recv + new String(packet.data);
                    if (!recv.equals("")) {
                        String outString = "b";
                        for (byte b : recv.getBytes())
                            outString = outString + " " + b;
                        outString = outString + "\ns " + recv;
                        System.out.println(outString);
                        recv = "";
                    }
                };

                @Override
                public void disconnect(Disconnect reason) {
                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
                }
            };
            System.out.println(net.getSocket().getLocalSocketAddress().toString());
            while (true) {
                String in = input.readLine();
                if (in.startsWith("b ")) {
                    in = in.substring(2);
                    String[] bytes = in.split(" ");
                    byte[] bArray = new byte[bytes.length];
                    for (int i = 0; i < bytes.length; i++) {
                        bArray[i] = (byte) (int) Integer.valueOf(bytes[i]);
                    }
                    net.send(new RawPacket(bArray));
                } else if (in.startsWith("s ")) {
                    in = in.substring(2);
                    net.send(new RawPacket(in.getBytes()));
                }
            }
        }
    }

}