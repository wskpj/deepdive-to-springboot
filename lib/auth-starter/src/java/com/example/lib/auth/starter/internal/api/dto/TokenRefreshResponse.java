package com.example.lib.auth.starter.internal.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 토큰 재발급 응답 DTO.
 */
@Schema(description = "토큰 재발급 성공 응답")
public record TokenRefreshResponse(
    @Schema(description = "새로 발급된 JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    String accessToken
) {
}
