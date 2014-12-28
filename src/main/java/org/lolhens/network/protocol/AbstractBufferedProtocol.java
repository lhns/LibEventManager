package org.lolhens.network.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBufferedProtocol<P> extends AbstractBlockingProtocol<P> {
    private ByteBuffer outBuffer = null;

    @Override
    protected final void onPacket(P packet) throws IOException {
        //Wrap packet into ByteBuffer
        ByteBuffer data = this.wrapPacket(packet);
        data.rewind();
        int size = data.remaining();

        outBuffer = ByteBuffer.allocate(4 + size).order(ByteOrder.BIG_ENDIAN);
        //Insert size of Buffer into itself
        outBuffer.putInt(size);
        outBuffer.put(data);

        //Rewind Buffer for writing and block any new packets
        outBuffer.rewind();
        setWriting(true);
    }

    @Override
    protected final void onWrite(WritableByteChannel out) throws IOException {
        out.write(outBuffer);
        if (outBuffer.remaining() == 0) {
            outBuffer = null;
            setWriting(false);
        }
    }

    private final ByteBuffer sizeBuf = ByteBuffer.allocate(4).order(ByteOrder.BIG_ENDIAN);
    private ByteBuffer packetBuf;

    @Override
    protected final void read(ReadableByteChannel in) throws IOException {
        //Read size buffer first
        in.read(sizeBuf);

        //Start reading packet buffer if size buffer was filled
        if (!sizeBuf.hasRemaining()) {
            //Allocate Packet Buffer if necessary
            if (packetBuf == null) {
                int size = sizeBuf.getInt(0);
                packetBuf = ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN);
            }
            in.read(packetBuf);
            if (!packetBuf.hasRemaining()) {
                packetBuf.rewind();
                receive(readPacket(packetBuf));
                sizeBuf.clear();

                packetBuf = null;
            }
        }
    }

    protected abstract ByteBuffer wrapPacket(P packet);

    protected abstract P readPacket(ByteBuffer input);
}
