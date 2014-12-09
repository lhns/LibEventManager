package com.dafttech.network.disconnect;

import com.dafttech.network.ProtocolProvider;

import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class DisconnectReason {
    public final ProtocolProvider<?> protocolProvider;
    public final IOException exception;
    public final boolean remote;
    private final String msg;

    public DisconnectReason(ProtocolProvider<?> protocolProvider, IOException exception, boolean remote, String msg) {
        this.protocolProvider = protocolProvider;
        this.exception = exception;
        this.remote = remote;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return msg;
    }
}
