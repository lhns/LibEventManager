package com.dafttech.network;

import com.dafttech.network.disconnect.DisconnectReason;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class AbstractExceptionHandler implements Closeable {
    protected ProtocolProvider<?> protocolProvider;

    protected abstract void handle(IOException exception);

    protected final void onDisconnect(DisconnectReason reason) {
        protocolProvider.onDisconnect(reason);
    }

    @Override
    public final void close() {
        try {
            protocolProvider.onClose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
