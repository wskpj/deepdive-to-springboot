package com.example.lib.web.core.exception;

public class ForbiddenException extends WebException {

    public ForbiddenException() {
        super(WebError.FORBIDDEN);
    }
}
