package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.nio.Server;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        Server<SimplePacket> server = null;
        try {
            server = new Server<>(SimpleProtocol.class, 12345, (abstractClient, packet) -> System.out.println(packet), (pp, r) -> System.out.println(r));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (true) server.broadcast(new SimplePacket(1, input.readLine().getBytes()));
    }
}
