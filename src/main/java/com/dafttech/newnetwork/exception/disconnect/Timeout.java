package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

public class Timeout extends DisconnectReason {
    public Timeout(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, "Timeout");
    }
}
