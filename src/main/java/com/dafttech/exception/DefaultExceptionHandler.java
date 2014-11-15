package com.dafttech.exception;

/**
 * Created by LolHens on 15.11.2014.
 */
public class DefaultExceptionHandler extends AbstractExceptionHandler<Exception> {
    public static final DefaultExceptionHandler instance = new DefaultExceptionHandler();

    private DefaultExceptionHandler() {
    }

    @Override
    public void handle(Exception exception) {
        throw new RuntimeException(exception);
    }
}
