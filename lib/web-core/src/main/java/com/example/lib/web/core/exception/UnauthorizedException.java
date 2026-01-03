package com.example.lib.web.core.exception;

public class UnauthorizedException extends WebException {

    public UnauthorizedException() {
        super(WebError.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(WebError.UNAUTHORIZED, message);
    }
}
