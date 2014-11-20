package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

public class Quit extends DisconnectReason {
    public Quit(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, "Quit");
    }
}
