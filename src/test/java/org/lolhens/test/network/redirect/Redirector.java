package org.lolhens.test.network.redirect;

import org.lolhens.network.AbstractClient;
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
        InputStream inputStream;
        //inputStream = new FileInputStream("redirect.txt");

        inputStream = new ByteArrayInputStream(
                "1248 localhost:1234".getBytes()
        );

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

        RedirectContainer redirectContainer = new RedirectContainer(host);
        server.setAcceptHandler(redirectContainer::onAccept);
        server.setReceiveHandler(redirectContainer::onReceive);

        server.bind(port);
    }

    private class RedirectContainer {
        private final String host;
        private volatile AbstractClient<byte[]> client;

        public RedirectContainer(String host) {
            this.host = host;
        }

        public void onAccept(AbstractClient<byte[]> c) {
            Client<byte[]> newClient = new Client<>(RawProtocol.class);

            newClient.setReceiveHandler((c2, packet) -> {
                c.send(packet);
            });

            try {
                newClient.connect(host);
            } catch (IOException e) {
                e.printStackTrace();
            }

            client = newClient;
        }

        public void onReceive(AbstractClient<byte[]> c, byte[] packet) {
            while (client == null) Thread.yield();
            client.send(packet);
        }
    }
}
