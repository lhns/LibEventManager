package com.dafttech.newnetwork.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

public abstract class AbstractProtocol<P> {
    protected ProtocolProvider<P> protocolProvider = null;

    protected abstract void encode(P packet, OutputStream outputStream);

    protected abstract void decode(InputStream inputStream, Consumer<P> submitPacket);

    public void close() {
    }
}