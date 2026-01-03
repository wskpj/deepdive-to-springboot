package com.example.lib.web.core.dto;

import java.util.HashMap;
import java.util.Map;

import com.example.lib.web.core.exception.WebError;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 웹 응답에서 에러가 발생했을 때 클라이언트에게 전달되는 표준 에러 모델
 * HTTP 상태 코드, 내부 에러 코드, 메시지, 발생 경로(URI) 및 상세 정보를 포함함
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
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
