package com.dafttech.network.test.soundsend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.dafttech.network.Client;
import com.dafttech.network.NetworkInterface;
import com.dafttech.network.disconnect.Disconnect;
import com.dafttech.network.packet.RawPacket;
import com.dafttech.network.protocol.RawProtocol;

public class SoundSend {
    public static NetworkInterface<RawPacket> net;
    public static TargetDataLine line;

    public static void main(String[] args) throws UnknownHostException, IOException, LineUnavailableException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Address/Port: ");
        String address = input.readLine();

        net = new Client<RawPacket>(RawProtocol.class, address) {
            @Override
            public void receive(RawPacket packet) {
            };

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
    }
}
