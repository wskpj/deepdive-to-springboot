package com.example.lib.exception.core;

public abstract class BaseDomainException extends BaseException {

    public BaseDomainException() {
        super();
    }

    public BaseDomainException(String message) {
        super(message);
    }

    public BaseDomainException(Object details) {
        super(details);
    }

    public BaseDomainException(String message, Object details) {
        super(message, details);
    }

    public BaseDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseDomainException(String message, Object details, Throwable cause) {
        super(message, details, cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
