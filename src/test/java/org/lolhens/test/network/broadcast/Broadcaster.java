package org.lolhens.test.network.broadcast;

import org.lolhens.network.nio.Server;
import org.lolhens.network.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 09.02.15.
 */
public class Broadcaster {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<byte[]> server = new Server<>(RawProtocol.class);

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
