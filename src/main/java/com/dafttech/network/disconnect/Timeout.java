package com.dafttech.network.disconnect;

import com.dafttech.network.ProtocolProvider;

import java.io.IOException;

public class Timeout extends DisconnectReason {
    public Timeout(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "Timeout");
    }
}
