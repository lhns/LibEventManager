package org.lolhens.network.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractBufferedProtocol<P> extends AbstractBlockingProtocol<P> {
    private ByteBuffer outBuffer = null;

    @Override
    protected final void onPacket(P packet) throws IOException {
        //Wrap packet into ByteBuffer
        outBuffer = this.wrapPacket(packet);

        //Insert size of Buffer into itself
        outBuffer.putInt(0, outBuffer.position());

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

    private final ByteBuffer sizeBuf = ByteBuffer.allocate(4);
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
                packetBuf = ByteBuffer.allocate(size);
            }

            if (packetBuf.hasRemaining()) {
                in.read(packetBuf);
            } else {
                receive(readPacket(packetBuf));
                sizeBuf.clear();

                packetBuf = null;
            }
        }
    }

    protected abstract ByteBuffer wrapPacket(P packet);

    protected abstract P readPacket(ByteBuffer input);
}
