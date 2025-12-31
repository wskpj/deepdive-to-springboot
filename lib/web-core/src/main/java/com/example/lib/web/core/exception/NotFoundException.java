package com.example.lib.web.core.exception;

public class NotFoundException extends WebException {

    public NotFoundException() {
        super(WebError.NOT_FOUND);
    }
}
