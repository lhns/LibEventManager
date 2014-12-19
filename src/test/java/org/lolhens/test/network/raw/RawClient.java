package org.lolhens.test.network.raw;

import org.lolhens.network.nio.Client;
import org.lolhens.network.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 27.11.2014.
 */
public class RawClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<byte[]> client = null;
        try {
            client = new Client<>(RawProtocol.class, (c, packet) -> {
                for (byte b : packet) System.out.print(b + " ");
                System.out.println();
                System.out.println(new String(packet));
            }, (pp, r) -> System.out.println(pp + ": " + r));
            client.connect(input.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) {
                if (in.startsWith("b ")) {
                    in = in.substring(2);
                    String[] byteStrings = in.split(" ");
                    byte[] bytes = new byte[byteStrings.length];
                    try {
                        for (int i = 0; i < byteStrings.length; i++) bytes[i] = Byte.valueOf(byteStrings[i]);
                        client.send(bytes);
                    } catch (NumberFormatException e) {
                    }
                } else {
                    client.send(in.getBytes());
                }
            }
        }
    }
}
