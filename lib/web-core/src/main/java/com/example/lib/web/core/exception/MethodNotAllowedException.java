package com.example.lib.web.core.exception;

public class MethodNotAllowedException extends WebException {

    public MethodNotAllowedException() {
        super(WebError.METHOD_NOT_ALLOWED);
    }

    public MethodNotAllowedException(String message) {
        super(WebError.METHOD_NOT_ALLOWED, message);
    }
}
