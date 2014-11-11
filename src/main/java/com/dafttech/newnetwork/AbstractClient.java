package com.dafttech.newnetwork;

import com.dafttech.newnetwork.protocol.AbstractProtocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.BiConsumer;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    public AbstractClient(Class<? extends AbstractProtocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) {
        super(protocolClazz, receive);
    }

    protected final void read(ReadableByteChannel in) {
        decode(in, (packet) -> receive.accept(this, packet));
    }

    protected abstract WritableByteChannel getWritableByteChannel();

    public final void send(P packet) {
        encode(packet, getWritableByteChannel());
    }
}
