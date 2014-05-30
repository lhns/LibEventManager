package com.dafttech.network.test.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.dafttech.network.Client;
import com.dafttech.network.Server;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.SimplePacket;
import com.dafttech.network.protocol.SimpleProtocol;

public class Chat {
    static boolean isServer = false;
    static Server<SimplePacket> server = null;
    static Client<SimplePacket> client = null;

    /**
     * @param args
     * @throws IOException
     * @throws NumberFormatException
     */
    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Server? ");
        isServer = Boolean.valueOf(input.readLine());
        System.out.println(isServer);
        System.out.println("Port: ");
        int port = Integer.valueOf(input.readLine());
        if (isServer) {
            try {
                server = new Server<SimplePacket>(SimpleProtocol.class, port) {
                    @Override
                    public void receive(Client<SimplePacket> client, SimplePacket packet) {
                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": " + packet.channel + ": "
                                + packet.toString());
                    }

                    @Override
                    public void disconnect(Client<SimplePacket> client, Disconnect reason) {
                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Disconnect "
                                + reason.toString());
                    }
                };
            } catch (Exception e) {
                System.out.println(e);
            }
            System.out.println(server.getServerSocket().getLocalSocketAddress().toString());
            while (true)
                server.send(new SimplePacket(10, input.readLine().getBytes()));
        } else {
            System.out.println("IP: ");
            String ip = input.readLine();
            client = new Client<SimplePacket>(SimpleProtocol.class, ip, port) {
                @Override
                public void receive(SimplePacket packet) {
                    System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": " + packet.channel + ": "
                            + packet.toString());
                }

                @Override
                public void disconnect(Disconnect reason) {
                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
                }
            };
            System.out.println(client.getSocket().getLocalSocketAddress().toString());
            client.send(new SimplePacket(1, "Connected!".getBytes()));
            while (true)
                client.send(new SimplePacket(10, input.readLine().getBytes()));
        }
    }
}
