package com.dafttech.eventmanager;

public class QueueOverflowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public QueueOverflowException() {
    }

    public QueueOverflowException(String paramString) {
        super(paramString);
    }

    public QueueOverflowException(String paramString, Throwable paramThrowable) {
        super(paramString, paramThrowable);
    }

    public QueueOverflowException(Throwable paramThrowable) {
        super(paramThrowable);
    }
}