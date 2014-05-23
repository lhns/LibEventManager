//package com.dafttech.test;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import com.dafttech.network.Client;
//import com.dafttech.network.Client.Disconnect;
//import com.dafttech.network.Server;
//
//public class RawChat {
//    static boolean isServer = false;
//    static Server server = null;
//    static Client client = null;
//    static int count = 0;
//    static String recv = "";
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
//                    public void receiveRaw(Client client, InputStream inputStream) throws IOException {
//                        if (inputStream.available() > 0) {
//                            byte b = (byte) client.readRaw();
//                            recv = recv + new String(new byte[] { b });
//                            // System.out.print(b);
//                        } else if (!recv.equals("")) {
//                            String outString = "b";
//                            for (byte b : recv.getBytes())
//                                outString = outString + " " + b;
//                            outString = outString + "\ns " + recv;
//                            System.out.println(outString);
//                            recv = "";
//                        }
//                    };
//
//                    @Override
//                    public void connect(Client client) {
//                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Connection Requested");
//                    }
//
//                    @Override
//                    public void disconnect(Client client, Disconnect reason) {
//                        System.out.println(client.getSocket().getRemoteSocketAddress().toString() + ": Disconnect "
//                                + reason.toString());
//                    }
//                };
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            System.out.println(server.getServerSocket().getLocalSocketAddress().toString());
//            while (true)
//                server.broadcastRaw(input.readLine().getBytes());
//        } else {
//            System.out.println("IP: ");
//            String ip = input.readLine();
//            client = new Client(ip, port) {
//                @Override
//                public void receiveRaw(InputStream inputStream) throws IOException {
//                    if (inputStream.available() > 0) {
//                        byte b = (byte) readRaw();
//                        recv = recv + new String(new byte[] { b });
//                        // System.out.print(b);
//                    } else if (!recv.equals("")) {
//                        String outString = "b";
//                        for (byte b : recv.getBytes())
//                            outString = outString + " " + b;
//                        outString = outString + "\ns " + recv;
//                        System.out.println(outString);
//                        recv = "";
//                    }
//                };
//
//                @Override
//                public void disconnect(Disconnect reason) {
//                    System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
//                }
//            };
//            System.out.println(client.getSocket().getLocalSocketAddress().toString());
//            while (true) {
//                String in = input.readLine();
//                if (in.startsWith("b ")) {
//                    in = in.substring(2);
//                    String[] bytes = in.split(" ");
//                    byte[] bArray = new byte[bytes.length];
//                    for (int i = 0; i < bytes.length; i++) {
//                        bArray[i] = (byte) (int) Integer.valueOf(bytes[i]);
//                    }
//                } else if (in.startsWith("s ")) {
//                    in = in.substring(2);
//                    client.sendRaw(in.getBytes());
//                }
//            }
//        }
//    }
//}
