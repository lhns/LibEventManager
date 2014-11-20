package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

public class Reset extends DisconnectReason {
    public Reset(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, "Connection Reset");
    }
}
