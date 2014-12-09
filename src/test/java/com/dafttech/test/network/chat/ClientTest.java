package com.dafttech.test.network.chat;

import com.dafttech.network.nio.Client;
import com.dafttech.network.packet.SimplePacket;
import com.dafttech.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<SimplePacket> client = null;
        try {
            client = new Client<>(SimpleProtocol.class, (c, packet) -> System.out.println(packet), (pp, r) -> System.out.println(pp + ": " + r));
            client.connect(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) client.send(new SimplePacket(1, in.getBytes()));
        }
    }
}
