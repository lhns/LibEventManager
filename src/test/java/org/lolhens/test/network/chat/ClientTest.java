package org.lolhens.test.network.chat;

import org.lolhens.network.nio.Client;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.AbstractBufferedProtocol;
import org.lolhens.network.protocol.SimpleProtocol;

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

        client.setReceiveHandler((c, packet) -> System.out.println(packet));
        client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

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
            ByteBuffer bb = ByteBuffer.allocate(packet.length()).order(ByteOrder.BIG_ENDIAN);
            bb.put(packet.getBytes());
            return null;
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
