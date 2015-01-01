package org.lolhens.network.protocol;

import org.lolhens.network.packet.SimplePacket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 11.11.2014.
 */
public class SimpleProtocol extends AbstractBlockingProtocol<SimplePacket> {
    private volatile ByteBuffer outBuffer = null;

    @Override
    protected void onPacket(SimplePacket packet) throws IOException {
        outBuffer = ByteBuffer.allocate(8 + packet.data.length).order(ByteOrder.BIG_ENDIAN);
        outBuffer.putInt(packet.channel);
        outBuffer.putInt(packet.data.length);
        outBuffer.put(packet.data);
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

    private final ByteBuffer inHeader = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
    private volatile ByteBuffer inData;

    @Override
    protected void read(ReadableByteChannel in) throws IOException {
        in.read(inHeader);
        if (!inHeader.hasRemaining()) {
            if (inData == null) inData = ByteBuffer.allocate(inHeader.getInt(4)).order(ByteOrder.BIG_ENDIAN);
            in.read(inData);
            if (!inData.hasRemaining()) {
                byte[] bytes = new byte[inData.capacity()];
                inData.rewind();
                inData.get(bytes);
                receive(new SimplePacket(inHeader.getInt(0), bytes));
                inHeader.clear();
                inData = null;
            }
        }
    }
}
