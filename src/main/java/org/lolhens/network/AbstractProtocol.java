package org.lolhens.network;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractProtocol<P> {
    protected AbstractClient<P> client = null;

    protected abstract void send(P packet);

    protected final void receive(P packet) {
        client.receive(packet);
    }

    protected abstract void write(WritableByteChannel out) throws IOException;

    protected abstract void read(ReadableByteChannel in) throws IOException;

    protected final void setWriteEnabled(boolean value) {
        client.setWriteEnabled(value);
    }

    protected final boolean isAlive() {
        return client.isAlive();
    }

    protected void onClose() {
    }
}