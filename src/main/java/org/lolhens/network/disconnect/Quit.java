package org.lolhens.network.disconnect;

import org.lolhens.network.ProtocolProvider;

import java.io.IOException;

public class Quit extends DisconnectReason {
    public Quit(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "Quit");
    }
}
