package com.example.lib.web.core.exception;

import com.example.lib.common.core.exception.HandledException;

public abstract class WebException extends HandledException {

    protected WebException(WebError webError) {
        super(webError);
    }
}
