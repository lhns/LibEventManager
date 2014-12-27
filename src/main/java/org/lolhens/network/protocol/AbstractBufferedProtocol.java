package org.lolhens.network.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import org.lolhens.network.packet.SimplePacket;

public abstract class AbstractBufferedProtocol<P> extends AbstractBlockingProtocol<P> {
    private ByteBuffer outBuffer = null;

    @Override
    protected final void onPacket(P packet) throws IOException {
        outBuffer = this.wrapPacket(packet);
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

    protected abstract ByteBuffer wrapPacket(P packet);
}
