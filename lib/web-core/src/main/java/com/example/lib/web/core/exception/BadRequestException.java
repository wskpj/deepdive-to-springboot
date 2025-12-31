package com.example.lib.web.core.exception;

public class BadRequestException extends WebException {

    public BadRequestException() {
        super(WebError.BAD_REQUEST);
    }
}
