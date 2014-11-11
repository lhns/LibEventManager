package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.packet.SimplePacket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.Consumer;

/**
 * Created by LolHens on 11.11.2014.
 */
public class SimpleProtocol extends AbstractProtocol<SimplePacket> {
    @Override
    protected void encode(SimplePacket packet, WritableByteChannel out) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(8 + packet.data.length).order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(packet.channel);
        buffer.putInt(packet.data.length);
        buffer.put(packet.data);
        out.write(buffer);
    }

    private ByteBuffer header = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN), data;

    @Override
    protected void decode(ReadableByteChannel in, Consumer<SimplePacket> submitPacket) throws IOException {
        in.read(header);
        if (!header.hasRemaining()) {
            if (data == null) data = ByteBuffer.allocate(header.getInt(4)).order(ByteOrder.BIG_ENDIAN);
            in.read(data);
            if (!data.hasRemaining()) {
                byte[] bytes = new byte[data.capacity()];
                data.get(bytes);
                submitPacket.accept(new SimplePacket(header.getInt(0), bytes));
                header.rewind();
                data = null;
            }
        }
    }
}
