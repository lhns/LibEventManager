package com.dafttech.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.dafttech.network.Client;
import com.dafttech.network.Client.Disconnect;
import com.dafttech.network.Server;

public class RawReceiver {
    public static String inHeader = "", outHeader = "", content = "";

    public static void main(String[] args) {
        try {
            new Server(new BufferedReader(new InputStreamReader(System.in)).readLine()) {
                @Override
                public void receiveRaw(Client client, InputStream inputStream) throws IOException {
                    inHeader = inHeader + new String(new byte[] { (byte) client.readRaw() });
                    if (inHeader.endsWith("\r\n\r\n")) {
                        content = "{\"name\":\"nothingspecial\","
                                + "\"displayName\":\" Minecraft: Nothing Special\","
                                + "\"user\":\"LolHens\","
                                + "\"version\":\"1.0\","
                                + "\"url\":\"https://dl.dropboxusercontent.com/u/148704233/Minecraft%20-%20The%20Next%20Generation.zip\","
                                + "\"minecraft\":\"1.6.4\","
                                + "\"logo\":{\"url\":\"http://cdn.technicpack.net/platform/pack-logos/331789.png?1397635247\","
                                + "\"md5\":\"5023e4fcf89d695a820b422a12331f5a\"},"
                                + "\"background\":{\"url\":\"http://cdn.technicpack.net/platform/pack-backgrounds/331789.png?1397635247\","
                                + "\"md5\":\"dc1a3cc155715b4730bd14f8f2e1ecc5\"}," + "\"solder\":\"\"," + "\"forceDir\":false}";
                        content = content.replace("/", "\\/");
                        outHeader = "HTTP/1.0 200 OK\r\nServer: LolHens/1.0 (Windows)\r\nContent-Length: "
                                + content.length()
                                + "\r\nContent-Language: en\r\nContent-Type: text/plain; charset=utf-8\r\nConnection: close\r\n\r\n"
                                + content;
                        client.sendRaw(outHeader.getBytes());

                        // System.out.println(inHeader);
                        // System.out.println(outHeader);
                        inHeader = "";

                    }
                }

                @Override
                public void connect(Client client) {
                    System.out.println("connect: " + client.getSocket().getRemoteSocketAddress().toString());
                }

                @Override
                public void disconnect(Client client, Disconnect reason) {
                    System.out.println("disconnect: " + client.getSocket().getRemoteSocketAddress().toString());
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
