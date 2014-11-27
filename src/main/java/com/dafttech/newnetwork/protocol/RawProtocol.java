package com.dafttech.newnetwork.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 27.11.2014.
 */
public class RawProtocol extends AbstractBlockingProtocol<byte[]> {
    private ByteBuffer outData;

    @Override
    protected void onPacket(byte[] packet) throws IOException {
        outData = ByteBuffer.allocate(packet.length).order(ByteOrder.BIG_ENDIAN);
        outData.put(packet);
        outData.rewind();
        setWriting(true);
    }

    @Override
    protected void onWrite(WritableByteChannel out) throws IOException {
        out.write(outData);
        if (outData.remaining() == 0) {
            outData = null;
            setWriting(false);
        }
    }

    private static final int bufferSize = 32;
    private ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize).order(ByteOrder.BIG_ENDIAN);
    private byte[] byteArray = new byte[bufferSize];

    @Override
    protected void read(ReadableByteChannel in) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int bytes;
        do {
            bytes = in.read(byteBuffer);
            if (bytes < 0) return;
            byteBuffer.rewind();
            byteBuffer.get(byteArray, 0, bytes);
            byteStream.write(byteArray, 0, bytes);
            byteBuffer.clear();
        } while (bytes == bufferSize);
        receive(byteStream.toByteArray());
    }
}
