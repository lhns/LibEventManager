package com.dafttech.network.disconnect;

import com.dafttech.network.ProtocolProvider;

import java.io.IOException;

public class Reset extends DisconnectReason {
    public Reset(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, true, "Connection Reset");
    }
}
