package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.nio.Client;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.IOException;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) {
        try {
            Client client = new Client<SimplePacket>(SimpleProtocol.class, "127.0.0.1", 1234, (ProtocolProvider<SimplePacket> protocolProvider, SimplePacket packet) -> System.out.println(packet));
            System.out.println("finish");
            client.send(new SimplePacket(2, "Hallo!".getBytes()));
            Thread.sleep(1000);
            client.send(new SimplePacket(2, "Hallo2!".getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
