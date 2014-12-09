package com.dafttech.test.newnetwork.chat;

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
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<SimplePacket> server = null;
        try {
            server = new Server<>(SimpleProtocol.class, (c, packet) -> System.out.println(packet), (pp, r) -> System.out.println(pp + ": " + r));
            server.bind(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) server.broadcast(new SimplePacket(1, in.getBytes()));
        }
    }
}
