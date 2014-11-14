package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.nio.Client;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.IOException;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        try {
            Client client = new Client<SimplePacket>(SimpleProtocol.class, "127.0.0.1", 12345, (ProtocolProvider<SimplePacket> protocolProvider, SimplePacket packet) -> System.out.println(packet));
            client.send(new SimplePacket(2, "Hallo1!".getBytes()));
            client.send(new SimplePacket(2, "Hallo2!".getBytes()));
            client.send(new SimplePacket(2, "Hallo3!".getBytes()));
            client.send(new SimplePacket(2, "Hallo4!".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
