package com.example.lib.web.core.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.lib.web.core.exception.WebError;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
    int status,
    String code,
    String message,
    String instance,
    Map<String, Object> details
) {
    public static ApiError of(WebError error, String uri) {
        return new ApiError(
            error.getStatus(),
            error.getCode(),
            error.getMessage(),
            uri,
            new HashMap<>()
        );
    }

    public void addDetail(String key, Object value) {
        details.put(key, value);
    }
}
