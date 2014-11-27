package com.dafttech.test.newnetwork;

import com.dafttech.newnetwork.nio.Client;
import com.dafttech.newnetwork.packet.SimplePacket;
import com.dafttech.newnetwork.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Client<SimplePacket> client = null;
        try {
            client = new Client<>(SimpleProtocol.class, "127.0.0.1", 12345, (abstractClient, packet) -> System.out.println(packet), (pp, r) -> System.out.println(r));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        while (true) client.send(new SimplePacket(1, input.readLine().getBytes()));
    }
}
