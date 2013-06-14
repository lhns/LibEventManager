package com.dafttech.eventmanager.exception;

public class MissingEventTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MissingEventTypeException() {
    }

    public MissingEventTypeException(String paramString) {
        super(paramString);
    }

    public MissingEventTypeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public MissingEventTypeException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}
