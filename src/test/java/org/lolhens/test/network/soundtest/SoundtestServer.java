package org.lolhens.test.network.soundtest;

import org.lolhens.network.nio.Server;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 10.01.2015.
 */
public class SoundtestServer {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<SimplePacket> server = new Server<>(SimpleProtocol.class);

        server.setReceiveHandler((c, packet) -> {
            //server.broadcast((c2) -> c2 != c, packet);
            //System.out.println(packet);
            server.broadcast(packet);
        });
        server.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        System.out.println("port:");
        try {
            server.bind(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("name:");
        //String name = input.readLine();

        while (input != null) {
            String in = input.readLine();
            //if (in != null) server.broadcast(new SimplePacket(0, (name + ": " + in).getBytes()));
        }
    }
}
