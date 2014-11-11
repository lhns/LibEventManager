package com.dafttech.newnetwork.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Consumer;

/**
 * Created by LolHens on 11.11.2014.
 */
public abstract class AbstractPacketProtocol<P> extends AbstractProtocol<P> {
    @Override
    protected final void encode(P packet, OutputStream outputStream) {
        try {
            outputStream.write(encode(packet));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected final void decode(InputStream inputStream, Consumer<P> submitPacket) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            decode(bytes, submitPacket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract byte[] encode(P packet) throws IOException;

    protected abstract void decode(byte[] bytes, Consumer<P> submitPacket) throws IOException;
}
