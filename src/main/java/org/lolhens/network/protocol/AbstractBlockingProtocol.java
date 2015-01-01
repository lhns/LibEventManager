package org.lolhens.network.protocol;

import org.lolhens.network.AbstractProtocol;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 14.11.2014.
 */
public abstract class AbstractBlockingProtocol<P> extends AbstractProtocol<P> {
    private volatile P packet;
    private volatile boolean writing = false;

    @Override
    protected final void send(P packet) {
        while (this.packet != null && isAlive()) {
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException e) {
            }
        }
        synchronized (this) {
            this.packet = packet;
            super.setWriting(true);
        }
    }

    private final P popPacket() {
        if (this.packet == null) return null;
        P packet = this.packet;
        this.packet = null;
        synchronized (this) {
            this.notify();
        }
        return packet;
    }

    @Override
    protected final void write(WritableByteChannel out) throws IOException {
        if (!writing && packet != null) {
            onPacket(popPacket());
        }
        if (writing) {
            onWrite(out);
        }
    }

    protected abstract void onPacket(P packet) throws IOException;

    protected abstract void onWrite(WritableByteChannel out) throws IOException;

    @Override
    protected final void setWriting(boolean value) {
        if (value != writing) {
            if (!value) {
                synchronized (this) {
                    if (packet == null) super.setWriting(false);
                }
            }
        }
        writing = value;
    }
}
