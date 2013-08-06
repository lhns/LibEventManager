package com.dafttech.eventmanager.exception;

public class AsyncEventQueueOverflowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AsyncEventQueueOverflowException() {
    }

    public AsyncEventQueueOverflowException(String paramString) {
        super(paramString);
    }

    public AsyncEventQueueOverflowException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public AsyncEventQueueOverflowException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}