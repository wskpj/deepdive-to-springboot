package com.example.lib.exception.core;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    protected final Object details;

    protected BaseException() {
        super();
        this.details = null;
    }

    protected BaseException(Object details) {
        super();
        this.details = details;
    }

    protected BaseException(String message) {
        super(message);
        this.details = null;
    }

    protected BaseException(String message, Object details) {
        super(message);
        this.details = details;
    }

    protected BaseException(String message, Throwable cause) {
        super(message, cause);
        this.details = null;
    }

    protected BaseException(String message, Object details, Throwable cause) {
        super(message, cause);
        this.details = details;
    }
}