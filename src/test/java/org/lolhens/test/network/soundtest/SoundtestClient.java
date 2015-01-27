package org.lolhens.test.network.soundtest;

import org.lolhens.network.nio.Client;
import org.lolhens.network.packet.SimplePacket;
import org.lolhens.network.protocol.SimpleProtocol;

import javax.sound.sampled.*;
import java.io.*;
import java.util.Arrays;

/**
 * Created by LolHens on 10.01.2015.
 */
public class SoundtestClient {
    private static final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, 44100, 16, 1, 2, 44100, true);

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Client<SimplePacket> client = new Client<>(SimpleProtocol.class);

        client.setReceiveHandler((c, packet) -> {
            try {
                byte audio[] = packet.data;
                InputStream inputStream = new ByteArrayInputStream(audio);
                final AudioFormat format = audioFormat;
                final AudioInputStream ais =
                        new AudioInputStream(inputStream, format,
                                audio.length / format.getFrameSize());
                final SourceDataLine line = AudioSystem.getSourceDataLine(audioFormat);
                line.open(format);
                line.start();

                Runnable runner = new Runnable() {
                    int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
                    byte buffer[] = new byte[bufferSize];

                    public void run() {
                        try {
                            int count;
                            while ((count = ais.read(
                                    buffer, 0, buffer.length)) != -1) {
                                if (count > 0) {
                                    line.write(buffer, 0, count);
                                }
                            }
                            line.drain();
                            line.close();
                        } catch (IOException e) {
                            System.err.println("I/O problems: " + e);
                            System.exit(-3);
                        }
                    }
                };
                Thread playThread = new Thread(runner);
                playThread.start();
            } catch (LineUnavailableException e) {
                System.err.println("Line unavailable: " + e);
                System.exit(-4);
            }
        });
        client.setDisconnectHandler((pp, r) -> System.out.println(pp + ": " + r));

        System.out.println("address:");
        try {
            client.connect(input.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("name:");
        //String name = input.readLine();

        try {
            final AudioFormat format = audioFormat;
            final TargetDataLine line = AudioSystem.getTargetDataLine(audioFormat);
            line.open(format);
            line.start();
            Runnable runner = new Runnable() {
                int bufferSize = (int) format.getSampleRate()
                        * format.getFrameSize();
                byte buffer[] = new byte[bufferSize];

                public void run() {
                    while (true) {
                        int count = line.read(buffer, 0, buffer.length);
                        if (count > 0) {
                            client.send(new SimplePacket(0, Arrays.copyOf(buffer, count)));
                        }
                    }
                }
            };
            Thread captureThread = new Thread(runner);
            captureThread.start();
        } catch (LineUnavailableException e) {
            System.err.println("Line unavailable: " + e);
            System.exit(-2);
        }
    }
}
