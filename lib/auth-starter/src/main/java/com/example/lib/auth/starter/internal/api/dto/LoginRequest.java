package com.example.lib.auth.starter.internal.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 공통 로그인 요청 DTO.
 * 도메인 종속성을 최소화하여 로그인 식별자(username)와 비밀번호(password)의 표준 구조를 가집니다.
 */
@Schema(description = "로그인 요청 정보")
public record LoginRequest(
    @Schema(description = "고유 로그인 식별자 (이메일, 로그인 ID, 사번 등)", example = "user@example.com")
    String username,

    @Schema(description = "비밀번호", example = "password123!")
    String password
) {
}
