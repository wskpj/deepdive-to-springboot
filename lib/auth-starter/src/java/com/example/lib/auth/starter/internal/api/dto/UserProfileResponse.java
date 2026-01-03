package com.example.lib.auth.starter.internal.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 프로필 정보 응답 DTO.
 * 도메인 결합도를 낮추기 위해 토큰 기반 인증에 필수적인 식별값과 권한 등 최소한의 정보만 리포트합니다.
 * 현재 시점 기준 Access Token 및 Refresh Token의 잔여 유효 시간(ms)을 함께 포함하여 클라이언트가 토큰 갱신 타이밍을 판단할 수 있도록 합니다.
 */
@Schema(description = "내 정보 프로필 응답")
public record UserProfileResponse(
    @Schema(description = "로그인한 사용자의 고유 데이터베이스 ID", example = "100")
    Long id,

    @Schema(description = "로그인한 사용자의 시스템 내 역할/권한", example = "CUSTOMER")
    String role,

    @Schema(description = "Access Token 잔여 유효 시간 (밀리초, 현재 시점 기준)", example = "3540000")
    long accessTokenExpiresIn,

    @Schema(description = "Refresh Token 잔여 유효 시간 (밀리초, 현재 시점 기준), 쿠키에 RT가 없으면 null", example = "604700000", nullable = true)
    Long refreshTokenExpiresIn
) {
}

