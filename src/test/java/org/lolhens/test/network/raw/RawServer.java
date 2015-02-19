package org.lolhens.test.network.raw;

import org.lolhens.network.nio.Server;
import org.lolhens.network.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

/**
 * Created by LolHens on 27.11.2014.
 */
public class RawServer {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<byte[]> server = new Server<>(RawProtocol.class);

        server.setReceiveHandler((c, packet) -> {
            for (byte b : packet) System.out.print(b + " ");
            System.out.println();
            System.out.println(new String(packet));
        });
        server.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        try {
            server.bind(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (input != null) {
            String in = input.readLine();
            if (in != null) {
                int times = 1;
                if (in.startsWith("repeat ")) {
                    String[] split = in.split(" ", 3);
                    times = Integer.parseInt(split[1]);
                    in = split[2];
                }
                for (int i1 = 0; i1 < times; i1++) {
                    if (in.startsWith("b ")) {
                        in = in.substring(2);
                        String[] byteStrings = in.replaceAll("\\\\i", "" + i1).split(" ");
                        byte[] bytes = new byte[byteStrings.length];
                        try {
                            for (int i = 0; i < byteStrings.length; i++) bytes[i] = Byte.valueOf(byteStrings[i]);
                            server.broadcast(bytes);
                        } catch (NumberFormatException e) {
                        }
                    } else {
                        server.broadcast(in.replaceAll("\\n", "\n").replaceAll("\\\\i", "" + i1).getBytes());
                    }
                }
            }
        }
    }
}
