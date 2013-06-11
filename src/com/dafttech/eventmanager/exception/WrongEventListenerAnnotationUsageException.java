package com.dafttech.eventmanager.exception;

public class WrongEventListenerAnnotationUsageException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WrongEventListenerAnnotationUsageException() {
    }

    public WrongEventListenerAnnotationUsageException(String paramString) {
        super(paramString);
    }

    public WrongEventListenerAnnotationUsageException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public WrongEventListenerAnnotationUsageException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}
