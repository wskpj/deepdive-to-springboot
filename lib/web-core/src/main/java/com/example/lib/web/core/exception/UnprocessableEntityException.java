package com.example.lib.web.core.exception;

public class UnprocessableEntityException extends WebException {

    public UnprocessableEntityException() {
        super(WebError.UNPROCESSABLE_ENTITY);
    }
}
