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

        Client<SimplePacket> client = null;
        try {
            client = new Client<>(SimpleProtocol.class);
            client.setReceiveHandler((c, packet) -> System.out.println(packet));
            client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));
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
