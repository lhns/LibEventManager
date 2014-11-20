package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class DisconnectReason {
    public final ProtocolProvider<?> protocolProvider;
    public final IOException exception;
    private final String msg;

    private IOException closeException = null;

    public DisconnectReason(ProtocolProvider<?> protocolProvider, IOException exception, String msg) {
        this.protocolProvider = protocolProvider;
        this.exception = exception;
        this.msg = msg;
        if (closeProtocolProvider()) {
            try {
                protocolProvider.close();
            } catch (IOException e) {
                closeException = e;
            }
        }
    }

    protected boolean closeProtocolProvider() {
        return true;
    }

    public final IOException getCloseException() {
        return closeException;
    }

    @Override
    public String toString() {
        return msg;
    }
}
