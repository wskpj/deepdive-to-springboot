package com.example.lib.web.core.dispatcher;

import com.example.lib.web.core.dto.ApiError;

public interface ExceptionResponseDispatcher {

    ApiError dispatch(Exception e, String uri);
}
