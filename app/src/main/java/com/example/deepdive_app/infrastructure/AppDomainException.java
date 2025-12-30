package com.example.deepdive_app.infrastructure;

import com.example.lib.exception.core.BaseDomainException;

public class AppDomainException extends BaseDomainException {
    public AppDomainException(String message) {
        super(message);
    }
}
