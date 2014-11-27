package com.dafttech.newnetwork.exception.disconnect;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;

/**
 * Created by LolHens on 27.11.2014.
 */
public class Refused extends DisconnectReason {
    public Refused(ProtocolProvider<?> protocolProvider, IOException exception) {
        super(protocolProvider, exception, "Connection Refused");
    }
}