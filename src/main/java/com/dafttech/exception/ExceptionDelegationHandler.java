package com.dafttech.exception;

import java.util.function.Consumer;

/**
 * Created by LolHens on 15.11.2014.
 */
public class ExceptionDelegationHandler extends AbstractExceptionHandler<Exception> {
    private final Consumer<Exception> exceptionHandler;

    public ExceptionDelegationHandler(Consumer<Exception> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void handle(Exception exception) {
        exceptionHandler.accept(exception);
    }
}
