package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.nio.Server;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.IOException;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ServerTest {
    public static void main(String[] args) {
        try {
            Server server = new Server<SimplePacket>(SimpleProtocol.class, 12345, (abstractClient, packet) -> System.out.println(packet));
            while (true) server.broadcast(new SimplePacket(2, "Hallo?".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
