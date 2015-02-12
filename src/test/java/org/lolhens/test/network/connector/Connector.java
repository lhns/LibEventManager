package org.lolhens.test.network.connector;

import org.lolhens.network.nio.Client;
import org.lolhens.network.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 09.02.15.
 */
public class Connector {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<byte[]> client1 = new Client<>(RawProtocol.class);
        Client<byte[]> client2 = new Client<>(RawProtocol.class);

        client1.setReceiveHandler((c, packet) -> client2.send(packet));
        client2.setReceiveHandler((c, packet) -> client1.send(packet));

        try {
            client1.connect(input.readLine());
            client2.connect(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            Thread.yield();
        }
    }
}
