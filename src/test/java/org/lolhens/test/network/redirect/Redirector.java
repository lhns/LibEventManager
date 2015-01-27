package org.lolhens.test.network.redirect;

import org.lolhens.network.nio.Client;
import org.lolhens.network.nio.Server;
import org.lolhens.network.protocol.RawProtocol;

import java.io.*;

/**
 * Created by LolHens on 27.11.2014.
 */
public class Redirector {
    private static Redirector instance = new Redirector();

    public static void main(String[] args) throws IOException {
        InputStream inputStream = new FileInputStream("redirect.txt");

        instance.parse(new InputStreamReader(inputStream));

        while (true) {
            Thread.yield();
        }
    }

    public void parse(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);

        String line = bufferedReader.readLine();
        while (line != null) {
            String[] split = line.split(" ");

            redirect(Integer.valueOf(split[0]), split[1]);

            line = bufferedReader.readLine();
        }
    }

    @SuppressWarnings("unchecked")
    public void redirect(int port, String host) throws IOException {
        Server<byte[]> server = new Server<>(RawProtocol.class);

        server.setAcceptHandler((c) -> {
            Client<byte[]> client = new Client<>(RawProtocol.class);

            client.setReceiveHandler((c2, packet) -> {
                c.send(packet);
            });

            try {
                client.connect(host);
            } catch (IOException e) {
                e.printStackTrace();
            }

            server.setAttachment(client);
        });

        server.setReceiveHandler((c, packet) -> {
            while (server.getAttachment() == null) {
                Thread.yield();
            }

            Client<byte[]> client = (Client<byte[]>) server.getAttachment();

            client.send(packet);
        });

        server.bind(port);
    }
}
