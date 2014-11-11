package com.dafttech.newnetwork;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractProtocol<P> implements Closeable {
    protected AbstractClient<P> client = null;

    public abstract void send(P packet);

    protected final void receive(P packet) {
        client.receive(packet);
    }

    protected abstract void write(WritableByteChannel out) throws IOException;

    protected abstract void read(ReadableByteChannel in) throws IOException;

    @Override
    public void close() {
    }
}