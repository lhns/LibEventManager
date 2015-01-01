package org.lolhens.test.network.chat;

import org.lolhens.network.nio.Client;
import org.lolhens.network.protocol.AbstractBufferedProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by LolHens on 11.11.2014.
 */
public class ClientTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<String> client = new Client<>(Testprotocol.class);

        client.setReceiveHandler((c, packet) -> {
            //c.send(packet);
            //c.send(packet + "-");
            System.out.println(packet);
        });
        client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));
        client.setConnectHandler((c) -> {
            //c.send("asdf");
            //c.send("asdf2");
            //for (int i = 0; i < 100; i++) client.send("" + i);
        });

        try {
            client.connect(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in.toLowerCase().startsWith("reconnect ")) client.connect(in.toLowerCase().replace("reconnect ", ""));
            if (in != null) client.send(in);
        }
    }

    public static class Testprotocol extends AbstractBufferedProtocol<String> {
        @Override
        protected ByteBuffer wrapPacket(String packet) {
            if (packet == null) System.out.println("NULL!!!");
            byte[] data = packet.getBytes();
            ByteBuffer bb = ByteBuffer.allocate(data.length).order(ByteOrder.BIG_ENDIAN);
            bb.put(data);
            return bb;
        }

        @Override
        protected String readPacket(ByteBuffer input) {
            int length = input.capacity();
            byte[] data = new byte[length];
            input.get(data);
            return new String(data);
        }
    }
}
