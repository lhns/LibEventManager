package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.AbstractProtocol;
import com.dafttech.newnetwork.packet.SimplePacket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 11.11.2014.
 */
public class SimpleProtocol extends AbstractProtocol<SimplePacket> {
    private SimplePacket packet;

    @Override
    public void send(SimplePacket packet) {
        if (this.packet == null) {
            this.packet = packet;
        }
        setWriteEnabled(true);
    }

    private ByteBuffer outBuffer = null;

    @Override
    protected void write(WritableByteChannel out) throws IOException {
        if (packet != null && outBuffer == null) {
            outBuffer = ByteBuffer.allocate(8 + packet.data.length).order(ByteOrder.BIG_ENDIAN);
            outBuffer.putInt(packet.channel);
            outBuffer.putInt(packet.data.length);
            outBuffer.put(packet.data);
            packet = null;
        }
        if (outBuffer != null) {
            outBuffer.rewind();
            out.write(outBuffer);
            if (outBuffer.remaining() == 0) {
                outBuffer = null;
                if (packet == null) setWriteEnabled(false);
            }
        }
    }

    private ByteBuffer inHeader = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN), inData;

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
