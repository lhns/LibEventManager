package com.dafttech.newnetwork.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

public class Timeout extends DisconnectReason {
    public Timeout(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "Timeout");
    }
}
