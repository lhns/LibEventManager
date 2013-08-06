package com.dafttech.eventmanager.exception;

public class UnknownEventTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownEventTypeException() {
    }

    public UnknownEventTypeException(String paramString) {
        super(paramString);
    }

    public UnknownEventTypeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public UnknownEventTypeException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}