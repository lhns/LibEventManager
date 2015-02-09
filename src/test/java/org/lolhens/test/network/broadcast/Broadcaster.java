package org.lolhens.test.network.broadcast;

import org.lolhens.network.nio.Server;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by pierre.kisters on 09.02.15.
 */
public class Broadcaster {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<SimplePacket> server = new Server<>(SimpleProtocol.class);

        server.setReceiveHandler((c, packet) -> {
            server.broadcast((client) -> client != c, packet);
        });
        server.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        try {
            server.bind(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Thread.yield();
        }
    }
}
