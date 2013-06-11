package com.dafttech.eventmanager.exception;

public class WrongEventManagerModeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WrongEventManagerModeException() {
    }

    public WrongEventManagerModeException(String paramString) {
        super(paramString);
    }

    public WrongEventManagerModeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public WrongEventManagerModeException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}
