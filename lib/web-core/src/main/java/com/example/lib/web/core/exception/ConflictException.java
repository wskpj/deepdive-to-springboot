package com.example.lib.web.core.exception;

public class ConflictException extends WebException {

    public ConflictException() {
        super(WebError.CONFLICT);
    }

    public ConflictException(String message) {
        super(WebError.CONFLICT, message);
    }
}
