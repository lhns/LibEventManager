package com.dafttech.network.disconnect;

import com.dafttech.network.ProtocolProvider;

import java.io.IOException;

public class EOF extends DisconnectReason {
    public EOF(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "End of Stream");
    }
}
