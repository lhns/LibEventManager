package com.dafttech.test.network;

public class SoundSend {
    // NOT PORTED FOR THE NEW NETWORK LIBRARY!
/*    public static NetworkInterface<RawPacket> net;
    public static TargetDataLine line;

    public static void main(String[] args) throws UnknownHostException, IOException, LineUnavailableException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Address/Port: ");
        String address = input.readLine();

        net = new Client<RawPacket>(RawProtocol.class, address) {
            @Override
            public void connect() {
                System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Connection Requested");
            }

            @Override
            public void disconnect(Disconnect reason) {
                System.out.println(getSocket().getRemoteSocketAddress().toString() + ": Disconnect " + reason.toString());
            }
        };
        System.out.println(net.getSocket().getLocalSocketAddress().toString());

        DataLine.Info nfo = new DataLine.Info(TargetDataLine.class, new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 4500, 8, 1,
                1, 4500, false));
        line = (TargetDataLine) AudioSystem.getLine(nfo);

        byte[] buffer = new byte[512];

        line.open();
        line.start();

        net.send(new RawPacket((byte) 211));
        while (true) {
            line.read(buffer, 0, buffer.length);
            net.send(new RawPacket(buffer));
        }
    }*/
}
