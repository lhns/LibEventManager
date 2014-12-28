package org.lolhens.network;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractProtocol<P> {
    private AbstractClient<P> client = null;
    private boolean writeEnabled = false;

    protected void setClient(AbstractClient<P> client) {
        this.client = client;
    }

    protected AbstractClient<P> getClient() {
        return client;
    }

    protected abstract void send(P packet);

    protected final void receive(P packet) {
        client.receive(packet);
    }

    protected abstract void write(WritableByteChannel out) throws IOException;

    protected abstract void read(ReadableByteChannel in) throws IOException;

    protected final void setWriteEnabled(boolean writeEnabled) {
        this.writeEnabled = writeEnabled;
        client.setWriteEnabled(writeEnabled);
        if (!writeEnabled && !client.isAlive()) {
            synchronized (client) {
                client.notify();
            }
        }
    }

    protected boolean isWriteEnabled() {
        return writeEnabled;
    }

    protected final boolean isAlive() {
        return client.isAlive();
    }

    protected void onClose() {
    }
}