package com.example.lib.web.core.exception;

public class TooManyRequestsException extends WebException {

    public TooManyRequestsException() {
        super(WebError.TOO_MANY_REQUESTS);
    }
}
