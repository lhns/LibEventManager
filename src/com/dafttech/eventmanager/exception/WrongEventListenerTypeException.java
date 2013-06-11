package com.dafttech.eventmanager.exception;

public class WrongEventListenerTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WrongEventListenerTypeException() {
    }

    public WrongEventListenerTypeException(String paramString) {
        super(paramString);
    }

    public WrongEventListenerTypeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public WrongEventListenerTypeException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}
