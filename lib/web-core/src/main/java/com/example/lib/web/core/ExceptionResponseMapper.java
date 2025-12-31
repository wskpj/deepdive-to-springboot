package com.example.lib.web.core;

public interface ExceptionResponseMapper {

    boolean supports(Exception e);

    ApiError map(Exception e, String uri);
}
