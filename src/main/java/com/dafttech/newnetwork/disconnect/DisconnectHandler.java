package com.dafttech.newnetwork.disconnect;

import com.dafttech.exception.AbstractExceptionHandler;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Created by LolHens on 15.11.2014.
 */
public class DisconnectHandler extends AbstractExceptionHandler<IOException> {
    private Consumer<DisconnectReason> disconnectHandler;

    public DisconnectHandler(Consumer<DisconnectReason> disconnectHandler) {
        this.disconnectHandler = disconnectHandler;
    }

    @Override
    public void handle(IOException exception) {
        exception.printStackTrace();
    }
}
