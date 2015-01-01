package org.lolhens.test.network.chat;

import org.lolhens.network.nio.Client;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<SimplePacket> client = new Client<>(SimpleProtocol.class);

        client.setReceiveHandler((c, packet) -> {
            System.out.println(packet.channel);
            c.send(new SimplePacket(packet.channel + 1, new byte[0]));
        });
        client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        client.setConnectHandler((c) -> c.send(new SimplePacket(0, new byte[0])));

        try {
            client.connect(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) client.send(new SimplePacket(Integer.valueOf(in), new byte[0]));
        }
    }
}
