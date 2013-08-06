package com.dafttech.eventmanager.exception;

public class WrongAnnotationUsageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WrongAnnotationUsageException() {
    }

    public WrongAnnotationUsageException(String paramString) {
        super(paramString);
    }

    public WrongAnnotationUsageException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public WrongAnnotationUsageException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}