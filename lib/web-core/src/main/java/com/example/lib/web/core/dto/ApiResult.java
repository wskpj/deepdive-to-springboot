package com.example.lib.web.core.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * API 응답의 표준 포맷 규격
 * 모든 웹 응답은 이 레코드 형태로 래핑되어 클라이언트에게 반환됨
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiResult<T>(
    boolean success,
    T data,
    ApiError error,
    String traceId,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime timestamp
) {
    public static <T> ApiResult<T> ok(T data, String traceId) {
        return new ApiResult<>(
            true,
            data,
            null,
            traceId,
            LocalDateTime.now()
        );
    }

    public static <T> ApiResult<T> fail(ApiError error, String traceId) {
        return new ApiResult<>(
            false,
            null,
            error,
            traceId,
            LocalDateTime.now()
        );
    }
}
