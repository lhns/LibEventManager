package com.dafttech.newnetwork.exception;

import com.dafttech.newnetwork.ProtocolProvider;
import com.dafttech.newnetwork.exception.disconnect.DisconnectReason;
import com.dafttech.newnetwork.exception.disconnect.EOF;
import com.dafttech.newnetwork.exception.disconnect.Reset;
import com.dafttech.newnetwork.exception.disconnect.Timeout;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Created by LolHens on 15.11.2014.
 */
public class ExceptionProcessor extends AbstractExceptionProcessor {
    @Override
    public DisconnectReason process(ProtocolProvider<?> protocolProvider, IOException exception) {
        if (exception instanceof EOFException) {
            return new EOF(protocolProvider, exception);
        } else if (exception instanceof SocketException && exception.getMessage().startsWith("Connection reset")) {
            return new Reset(protocolProvider, exception);
        } else if (exception instanceof SocketTimeoutException) {
            return new Timeout(protocolProvider, exception);
        }
        return null;
    }
}
