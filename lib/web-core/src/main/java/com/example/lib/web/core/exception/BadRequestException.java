package com.example.lib.web.core.exception;

public class BadRequestException extends WebException {

    public BadRequestException() {
        super(WebError.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(WebError.BAD_REQUEST, message);
    }
}
