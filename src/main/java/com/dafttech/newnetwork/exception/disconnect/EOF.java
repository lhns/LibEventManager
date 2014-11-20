package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

public class EOF extends DisconnectReason {
    public EOF(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, "End of Stream");
    }
}
