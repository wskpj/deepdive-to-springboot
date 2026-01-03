package com.example.lib.auth.starter.internal.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 로그인 응답 DTO.
 * 보안 강화를 위해 클라이언트 브라우저용 무상태 토큰(accessToken)과 기본 식별자(id, role) 정보만을 포함합니다.
 */
@Schema(description = "로그인 성공 응답")
public record LoginResponse(
    @Schema(description = "인증에 사용될 JWT Access Token", example = "eyJhbGciOiJIUzI1NiJ9...")
    String accessToken,

    @Schema(description = "로그인한 사용자의 고유 식별자", example = "100")
    Long id,

    @Schema(description = "로그인한 사용자의 시스템 내 역할/권한", example = "CUSTOMER")
    String role
) {
}
