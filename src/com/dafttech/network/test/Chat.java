package com.dafttech.network.test;
//package com.dafttech.test;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//
//import com.dafttech.network.Client;
//import com.dafttech.network.Client.Disconnect;
//import com.dafttech.network.Server;
//
//public class Chat {
//    static boolean isServer = false;
//    static Server server = null;
//    static Client client = null;
//
//    /**
//     * @param args
//     * @throws IOException
//     * @throws NumberFormatException
//     */
//    public static void main(String[] args) throws NumberFormatException, IOException {
//        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Server? ");
//        isServer = Boolean.valueOf(input.readLine());
//        System.out.println(isServer);
//        System.out.println("Port: ");
//        int port = Integer.valueOf(input.readLine());
//        if (isServer) {
//            try {
//                server = new Server(port) {
//                    @Override
//                    public void receive(Client client, int channel, byte[] data) {
//                        for (Client client1 : server.getClients())
//                            if (client1 != client) client1.send(channel, data);
//                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": " + new String(data));
//                    };
//
//                    @Override
//                    public void connect(Client client) {
//                        for (Client client1 : server.getClients())
//                            client1.send(1, (client.getSocket().getRemoteSocketAddress().toString() + ": Connection Requested")
//                                    .getBytes());
//                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Connection Requested");
//                    }
//
//                    @Override
//                    public void disconnect(Client client, Disconnect reason) {
//                        for (Client client1 : server.getClients())
//                            if (client1 != client)
//                                client1.send(1,
//                                        (client.getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason
//                                                .toString()).getBytes());
//                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Disconnect "
//                                + reason.toString());
//                    }
//                };
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            System.out.println(server.getServerSocket().getLocalSocketAddress().toString());
//            while (true)
//                server.broadcast(10, input.readLine().getBytes());
//        } else {
//            System.out.println("IP: ");
//            String ip = input.readLine();
//            client = new Client(ip, port) {
//                @Override
//                public void receive(int channel, byte[] data) {
//                    System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": " + new String(data));
//                    System.out.flush();
//                };
//
//                @Override
//                public void disconnect(Disconnect reason) {
//                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
//                }
//            };
//            System.out.println(client.getSocket().getLocalSocketAddress().toString());
//            client.send(1, "Connected!".getBytes());
//            while (true)
//                client.send(10, input.readLine().getBytes());
//        }
//    }
//}
