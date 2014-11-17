package com.dafttech.newnetwork.exception;

import com.dafttech.newnetwork.ProtocolProvider;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by LolHens on 15.11.2014.
 */
public class ExceptionHandler {
    private Consumer<DisconnectReason> disconnectHandler;

    public ExceptionHandler(Consumer<DisconnectReason> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    public void handle(ProtocolProvider<?> protocolProvider, IOException exception) {
        exception.printStackTrace();
    }
}
