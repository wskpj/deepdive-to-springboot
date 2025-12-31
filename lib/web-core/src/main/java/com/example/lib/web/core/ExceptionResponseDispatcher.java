package com.example.lib.web.core;

public interface ExceptionResponseDispatcher {

    ApiError dispatch(Exception e, String uri);
}
