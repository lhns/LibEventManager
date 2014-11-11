package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.io.Server;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.ProtocolProvider;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.IOException;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ServerTest {
    public static void main(String[] args) {
        try {
            Server server = new Server<SimplePacket>(SimpleProtocol.class, 1234, (ProtocolProvider<SimplePacket> protocolProvider, SimplePacket packet) -> System.out.println(packet));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
