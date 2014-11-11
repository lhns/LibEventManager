package com.dafttech.newnetwork;

import com.dafttech.newnetwork.protocol.Protocol;
import com.dafttech.newnetwork.protocol.ProtocolProvider;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.BiConsumer;

public abstract class AbstractClient<P> extends ProtocolProvider<P> {
    public AbstractClient(Class<? extends Protocol> protocolClazz, BiConsumer<ProtocolProvider<P>, P> receive) throws InstantiationException, IllegalAccessException {
        super(protocolClazz, receive);
    }

    protected final void read(InputStream inputStream) {
        decode(inputStream, (packet) -> receive.accept(this, packet));
    }

    protected abstract OutputStream getOutputStream() throws Exception;

    public final void send(P packet) {
        try {
            encode(packet, getOutputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
