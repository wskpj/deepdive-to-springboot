package com.example.lib.exception.core;

public abstract class BaseSystemException extends BaseException {

    public BaseSystemException() {
        super();
    }

    public BaseSystemException(String message) {
        super(message);
    }

    public BaseSystemException(Object details) {
        super(details);
    }

    public BaseSystemException(String message, Object details) {
        super(message, details);
    }

    public BaseSystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseSystemException(String message, Object details, Throwable cause) {
        super(message, details, cause);
    }
}
