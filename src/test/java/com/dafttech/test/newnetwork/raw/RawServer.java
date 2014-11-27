package com.dafttech.test.newnetwork.raw;

import com.dafttech.newnetwork.nio.Server;
import com.dafttech.newnetwork.protocol.RawProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LolHens on 27.11.2014.
 */
public class RawServer {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Server<byte[]> server = null;
        try {
            server = new Server<>(RawProtocol.class, input.readLine(), (c, packet) -> {
                for (byte b : packet) System.out.print(b + " ");
                System.out.println();
                System.out.println(new String(packet));
            }, (pp, r) -> System.out.println(pp + ": " + r));
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
                        server.broadcast(bytes);
                    } catch (NumberFormatException e) {
                    }
                } else {
                    server.broadcast(in.getBytes());
                }
            }
        }
    }
}
