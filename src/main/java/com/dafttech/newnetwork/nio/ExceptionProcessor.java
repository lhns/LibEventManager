package com.dafttech.newnetwork.nio;

import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.exception.AbstractExceptionProcessor;
import com.dafttech.newnetwork.exception.disconnect.*;

import java.io.EOFException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by LolHens on 15.11.2014.
 */
public class ExceptionProcessor extends AbstractExceptionProcessor {
    @Override
    public DisconnectReason process(ProtocolProvider<?> protocolProvider, IOException exception) {
        if (exception instanceof EOFException) {
            return new EOF(protocolProvider, exception);
        } else if (exception instanceof SocketTimeoutException) {
            return new Timeout(protocolProvider, exception);
        } else if (exception instanceof ConnectException) {
            return new Refused(protocolProvider, exception);
        }
        return new Reset(protocolProvider, exception);
    }
}
