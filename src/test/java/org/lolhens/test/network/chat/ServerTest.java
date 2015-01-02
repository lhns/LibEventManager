package org.lolhens.test.network.chat;

import org.lolhens.network.nio.Server;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<SimplePacket> server = new Server<>(SimpleProtocol.class);

        server.setReceiveHandler((c, packet) -> {
            System.out.println(new String(packet.data));
        });
        server.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        try {
            server.bind(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) server.broadcast(new SimplePacket(0, in.getBytes()));
        }
    }
}
