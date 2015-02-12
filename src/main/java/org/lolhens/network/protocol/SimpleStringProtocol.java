package org.lolhens.network.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 12.02.2015.
 */
public class SimpleStringProtocol extends AbstractBlockingProtocol<String> {
    private volatile ByteBuffer outBuffer = null;

    @Override
    protected void onPacket(String packet) throws IOException {
        byte[] data = packet.getBytes();
        outBuffer = ByteBuffer.allocate(4 + data.length).order(ByteOrder.BIG_ENDIAN);
        outBuffer.putInt(data.length);
        outBuffer.put(data);
        outBuffer.rewind();
        setWriting2(true);
    }

    @Override
    protected void onWrite(WritableByteChannel out) throws IOException {
        out.write(outBuffer);
        if (outBuffer.remaining() == 0) {
            outBuffer = null;
            setWriting2(false);
        }
    }

    private final ByteBuffer inHeader = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    private volatile ByteBuffer inData;

    @Override
    protected void read(ReadableByteChannel in) throws IOException {
        in.read(inHeader);
        if (!inHeader.hasRemaining()) {
            if (inData == null) inData = ByteBuffer.allocate(inHeader.getInt(0)).order(ByteOrder.BIG_ENDIAN);
            in.read(inData);
            if (!inData.hasRemaining()) {
                byte[] bytes = new byte[inData.capacity()];
                inData.rewind();
                inData.get(bytes);
                receive(new String(bytes));
                inHeader.clear();
                inData = null;
            }
        }
    }
}
