package com.dafttech.newnetwork;

import com.dafttech.newnetwork.disconnect.DisconnectReason;

import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class AbstractExceptionHandler {
    protected ProtocolProvider<?> protocolProvider;

    protected abstract void handle(IOException exception);

    protected final void onDisconnect(DisconnectReason reason) {
        protocolProvider.onDisconnect(reason);
    }
}
