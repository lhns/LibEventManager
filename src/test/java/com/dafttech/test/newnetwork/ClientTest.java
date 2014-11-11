package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.io.Client;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.ProtocolProvider;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.IOException;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) {
        try {
            Client client = new Client<SimplePacket>(SimpleProtocol.class, "localhost", 1234, (ProtocolProvider<SimplePacket> protocolProvider, SimplePacket packet) -> System.out.println(packet));
            client.send(new SimplePacket(2, "Hallo!".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
