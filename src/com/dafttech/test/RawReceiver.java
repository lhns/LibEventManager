package com.dafttech.test;

import java.io.IOException;

import com.dafttech.network.Client;
import com.dafttech.network.Client.Disconnect;
import com.dafttech.network.Server;

public class RawReceiver {
    public static String inHeader = "", outHeader = "", content = "";

    public static void main(String[] args) {
        try {
            new Server("80") {
                @Override
                public void receiveRaw(Client client) throws IOException {
                    inHeader = inHeader + new String(new byte[] { (byte) client.readRaw() });
                    if (inHeader.endsWith("\r\n\r\n")) {
                        content = "{\"name\":\"minecraft-the-next-generation\","
                                + "\"displayName\":\" Minecraft: The Next Generation\","
                                + "\"user\":\"AEnterprise\","
                                + "\"version\":\"1.0\","
                                + "\"url\":\"https:\\/\\/dl.dropboxusercontent.com\\/u\\/148704233\\/Minecraft%20-%20The%20Next%20Generation.zip\","
                                + "\"minecraft\":\"1.7.2\","
                                + "\"logo\":{\"url\":\"http:\\/\\/cdn.technicpack.net\\/platform\\/pack-logos\\/331789.png?1397635247\","
                                + "\"md5\":\"5023e4fcf89d695a820b422a12331f5a\"},"
                                + "\"background\":{\"url\":\"http:\\/\\/cdn.technicpack.net\\/platform\\/pack-backgrounds\\/331789.png?1397635247\","
                                + "\"md5\":\"dc1a3cc155715b4730bd14f8f2e1ecc5\"}," + "\"solder\":\"\"," + "\"forceDir\":false}";
                        outHeader = "HTTP/1.0 200 OK\r\nServer: LolHens/1.0 (Windows)\r\nContent-Length: "
                                + content.length()
                                + "\r\nContent-Language: en\r\nContent-Type: text/plain; charset=utf-8\r\nConnection: close\r\n\r\n"
                                + content;
                        client.sendRaw(outHeader.getBytes());

                        // System.out.print(inHeader);
                        // System.out.print(outHeader);
                        inHeader = "";

                    }
                }

                @Override
                public void connect(Client client) {
                    System.out.println("connect");
                }

                @Override
                public void disconnect(Client client, Disconnect reason) {
                    System.out.println("disconnect");
                }
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
