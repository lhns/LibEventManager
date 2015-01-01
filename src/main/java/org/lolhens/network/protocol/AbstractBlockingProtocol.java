package org.lolhens.network.protocol;

import org.lolhens.network.AbstractProtocol;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by LolHens on 14.11.2014.
 */
public abstract class AbstractBlockingProtocol<P> extends AbstractProtocol<P> {
    private volatile P packet;
    private volatile boolean writing = false;

    private ReentrantLock packetLock = new ReentrantLock();

    @Override
    protected final void send(P packet) {
        packetLock.lock();
        {
            while (this.packet != null) {
                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
            this.packet = packet;
            setWriting(true);
        }
        packetLock.unlock();
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
        if (!writing && packet != null) onPacket(popPacket());
        if (writing) onWrite(out);
    }

    protected abstract void onPacket(P packet) throws IOException;

    protected abstract void onWrite(WritableByteChannel out) throws IOException;

    protected final void setWriting2(boolean value) {
        if (value != writing && !value) {
            synchronized (this) {
                if (packet == null) setWriting(false);
            }
        }
        writing = value;
    }
}
