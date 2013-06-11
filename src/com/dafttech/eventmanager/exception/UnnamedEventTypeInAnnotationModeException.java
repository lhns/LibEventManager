package com.dafttech.eventmanager.exception;

public class UnnamedEventTypeInAnnotationModeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnnamedEventTypeInAnnotationModeException() {
    }

    public UnnamedEventTypeInAnnotationModeException(String paramString) {
        super(paramString);
    }

    public UnnamedEventTypeInAnnotationModeException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public UnnamedEventTypeInAnnotationModeException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}
