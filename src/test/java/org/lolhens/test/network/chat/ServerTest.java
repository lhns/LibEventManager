package org.lolhens.test.network.chat;

import org.lolhens.network.nio.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ServerTest {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<String> server = new Server<>(ClientTest.Testprotocol.class);

        server.setReceiveHandler((c, packet) -> System.out.println(packet));
        server.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        try {
            server.bind(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in.toLowerCase().startsWith("reconnect ")) server.bind(in.toLowerCase().replace("reconnect ", ""));
            if (in != null) server.broadcast(in);
        }
    }
}
