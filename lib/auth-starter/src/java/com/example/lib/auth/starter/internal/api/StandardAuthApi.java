package com.example.lib.auth.starter.internal.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.lib.auth.starter.internal.api.dto.LoginRequest;
import com.example.lib.auth.starter.internal.api.dto.LoginResponse;
import com.example.lib.auth.starter.internal.api.dto.TokenRefreshResponse;
import com.example.lib.auth.starter.internal.api.dto.UserProfileResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 회원가입 및 토큰 기반 인증 처리를 담당하는 공통 컨트롤러의 OpenAPI 표준 규격 정의 인터페이스.
 * 헥사고날 아키텍처의 인바운드 포트 진입점으로 연동되어 스웨거 사양 정의와 스프링 MVC 매핑의 책임을 동시에 충족합니다.
 */
@Tag(name = "인증 API", description = "로그인, 로그아웃, 토큰 재발급 및 내 정보 프로필 조회를 제공하는 공통 API 규격")
@RequestMapping("/api/auth")
public interface StandardAuthApi {

    /**
     * 로그인 자격 검증을 거쳐 인증 토큰을 생성합니다.
     *
     * @param request 로그인 정보 (로그인 식별자 및 암호 포함)
     * @return JWT Access Token 및 기본 유저 프로필 정보
     */
    @Operation(summary = "로그인", description = "아이디/패스워드를 검증하여 Access Token을 리턴하고, Refresh Token은 보안 쿠키(HTTP-Only, Secure, SameSite)에 세팅합니다.")
    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest request);

    /**
     * 쿠키에 담긴 Refresh Token을 통해 Access Token과 Refresh Token을 갱신합니다 (RTR 로테이션 기법).
     *
     * @return 갱신 발급된 신규 JWT Access Token
     */
    @Operation(summary = "토큰 재발급", description = "쿠키의 Refresh Token을 확인하여 RTR(Refresh Token Rotation) 기법으로 양방향 토큰을 갱신 발급합니다.")
    @PostMapping("/refresh")
    TokenRefreshResponse refresh();

    /**
     * 현재 로그인 세션을 완전 만료(로그아웃) 처리합니다.
     */
    @Operation(summary = "로그아웃", description = "쿠키 내 저장된 리프레시 토큰의 수명을 강제 만료시켜 인증 상태를 원천 폐기합니다.")
    @PostMapping("/logout")
    void logout();

    /**
     * 현재 요청을 보낸 사용자의 인증 토큰 정보를 파싱하여 상세 프로필 데이터를 응답합니다.
     *
     * @return 현재 서명 인증된 회원 상세 프로필 (ID, Role 포함)
     */
    @Operation(summary = "내 정보 조회", description = "Bearer Access Token 검증에 통과한 사용자의 ID 및 역할 정보를 반환합니다.")
    @GetMapping("/me")
    UserProfileResponse me(@RequestHeader(value = "Authorization", required = false) String authHeader);
}
