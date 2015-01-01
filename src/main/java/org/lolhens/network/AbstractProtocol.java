package org.lolhens.network;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public abstract class AbstractProtocol<P> {
    private volatile AbstractClient<P> client = null;

    protected abstract void send(P packet);

    protected final void receive(P packet) {
        client.receive(packet);
    }

    protected abstract void write(WritableByteChannel out) throws IOException;

    protected abstract void read(ReadableByteChannel in) throws IOException;


    // Setters

    protected void setClient(AbstractClient<P> client) {
        this.client = client;
    }

    protected final void setWriting(boolean writing) {
        client.setWriting(writing);

        // Notify because client tries to close
        if (!writing && !client.isAlive()) {
            synchronized (client) {
                client.notify();
            }
        }
    }

    protected void setClosed() {
    }

    // Getters

    protected AbstractClient<P> getClient() {
        return client;
    }

    protected boolean isWriting() {
        return client.isWriting();
    }

    protected final boolean isAlive() {
        return client.isAlive();
    }
}