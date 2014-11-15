package com.dafttech.exception;

/**
 * Created by LolHens on 15.11.2014.
 */
public abstract class AbstractExceptionHandler<E extends Throwable> {
    public abstract void handle(E exception);
}
