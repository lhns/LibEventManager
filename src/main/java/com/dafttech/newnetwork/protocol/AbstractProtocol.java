package com.dafttech.newnetwork.protocol;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.Consumer;

public abstract class AbstractProtocol<P> {
    protected ProtocolProvider<P> protocolProvider = null;

    protected abstract void encode(P packet, WritableByteChannel out) throws IOException;

    protected abstract void decode(ReadableByteChannel in, Consumer<P> submitPacket) throws IOException;

    public void close() {
    }
}