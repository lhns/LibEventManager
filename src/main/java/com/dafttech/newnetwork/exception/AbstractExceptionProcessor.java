package com.dafttech.newnetwork.exception;

import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.exception.disconnect.DisconnectReason;

import java.io.IOException;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class AbstractExceptionProcessor {
    public abstract DisconnectReason process(ProtocolProvider<?> protocolProvider, IOException exception);
}
