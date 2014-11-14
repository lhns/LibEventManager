package com.dafttech.newnetwork.protocol;

import com.dafttech.newnetwork.AbstractProtocol;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Created by LolHens on 14.11.2014.
 */
public abstract class AbstractBlockingProtocol<P> extends AbstractProtocol<P> {
    private P packet;
    private boolean writing = false;

    @Override
    protected final void send(P packet) {
        while (this.packet != null) {
            try {
                synchronized (this) {
                    this.wait(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (this.packet == null) this.packet = packet;
        setWriteEnabled(true);
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

    protected final void setWriting(boolean value) {
        if (value != writing) {
            if (value) {
            } else {
                if (packet == null) setWriteEnabled(false);
            }
        }
        writing = value;
    }
}
