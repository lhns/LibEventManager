package com.dafttech.network.disconnect;

import com.dafttech.network.ProtocolProvider;

import java.io.IOException;

public class Quit extends DisconnectReason {
    public Quit(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, false, "Quit");
    }
}
