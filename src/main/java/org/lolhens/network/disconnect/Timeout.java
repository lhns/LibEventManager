package org.lolhens.network.disconnect;

import org.lolhens.network.ProtocolProvider;

import java.io.IOException;

public class Timeout extends DisconnectReason {
    public Timeout(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, true, "Timeout");
    }
}
