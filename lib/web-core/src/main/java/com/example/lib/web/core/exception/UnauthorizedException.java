package com.example.lib.web.core.exception;


public class UnauthorizedException extends WebException {

    public UnauthorizedException() {
        super(WebError.UNAUTHORIZED);
    }
}
