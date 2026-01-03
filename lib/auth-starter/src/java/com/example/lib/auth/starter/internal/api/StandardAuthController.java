package com.example.lib.auth.starter.internal.api;

import org.springframework.web.bind.annotation.RestController;

import com.example.lib.auth.starter.internal.api.dto.LoginRequest;
import com.example.lib.auth.starter.internal.api.dto.LoginResponse;
import com.example.lib.auth.starter.internal.api.dto.TokenRefreshResponse;
import com.example.lib.auth.starter.internal.api.dto.UserProfileResponse;
import com.example.lib.auth.starter.internal.port.AuthPort;
import com.example.lib.auth.starter.internal.port.AuthUserDetail;
import com.example.lib.security.core.JwtProvider;
import com.example.lib.security.starter.internal.cookie.JwtCookieManager;
import com.example.lib.web.core.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;

/**
 * 헥사고날 아키텍처의 인바운드 웹 어댑터(Inbound Web Adapter) 구현체.
 * HTTP 요청 진입점으로서 요청을 해석하고 인증용 아웃바운드 포트(AuthPort) 인터페이스를 경유하여 도메인 비즈니스 레이어로 연동합니다.
 * 도메인 종속적 필드(이메일 등) 대신 추상 식별자인 'username'을 사용하여 다목적으로 이식이 가능합니다.
 */
@RestController
@RequiredArgsConstructor
public class StandardAuthController implements StandardAuthApi {

    /**
     * 회원가입, 로그인 검증, 프로필 조회를 위해 애플리케이션 코어와 협업하는 인바운드 SPI 포트.
     */
    private final AuthPort authPort;

    /**
     * HTTP-Only 및 Secure 기반 Refresh Token 쿠키 제어를 담당하는 공통 유틸리티.
     */
    private final JwtCookieManager jwtCookieManager;

    /**
     * JWT 토큰(AccessToken/RefreshToken) 생성을 처리하는 공통 보안 유틸리티.
     */
    private final JwtProvider<Long> jwtProvider;

    /**
     * [공통 로그인]
     * 자격 증명을 포트에 위임하여 검증받고, JWT 토큰을 발행한 후 리프레시 토큰은 HTTP-Only 쿠키로 숨깁니다.
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. 애플리케이션 코어에 로그인 자격 검증을 요청하여 추상 유저 명세(AuthUserDetail) 확보
        AuthUserDetail user = authPort.login(request.username(), request.password());

        // 2. JWT Access Token (메모리 저장용) & Refresh Token (쿠키 저장용) 발행
        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // 3. 브라우저 자바스크립트가 탈취할 수 없도록 리프레시 토큰을 Secure/HTTP-Only 쿠키에 영구 저장
        jwtCookieManager.setRefreshTokenCookie(refreshToken);

        // 4. 클라이언트에게는 무상태 API의 인증 증표인 Access Token 및 기본 유저 정보만 응답으로 송신
        return new LoginResponse(
                accessToken,
                user.getId(),
                user.getRole()
        );
    }

    /**
     * [공통 토큰 재발급 (RTR 기법)]
     * 탈취 방지를 위해 클라이언트가 보낸 쿠키 내 리프레시 토큰을 한 번 검증하고 새 한 쌍의 토큰을 로테이션하여 재인증합니다.
     */
    @Override
    public TokenRefreshResponse refresh() {
        // 1. 보안 쿠키에서 기존 Refresh Token 추출 시도
        String refreshToken = jwtCookieManager.getRefreshTokenCookie()
                .orElseThrow(UnauthorizedException::new);

        // 2. JWT 서명 및 만료 일자 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new UnauthorizedException();
        }

        // 3. 토큰 내부에 포함된 주체의 고유 식별자(ID) 추출
        Long userId = jwtProvider.getUserId(refreshToken);
        if (userId == null) {
            throw new UnauthorizedException();
        }

        // 4. 코어 서비스 영역에서 유효한 회원인지 재검증 및 유저 메타데이터 갱신
        AuthUserDetail user = authPort.loadUserById(userId);

        // 5. [RTR 핵심] 기존 토큰의 재사용 공격을 방어하기 위해 한 쌍의 새로운 Access/Refresh Token 생성
        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getRole());
        String newRefreshToken = jwtProvider.createRefreshToken(user.getId());

        // 6. 새로운 리프레시 토큰으로 기존 쿠키 값을 덮어써서 교체(Rotate)
        jwtCookieManager.setRefreshTokenCookie(newRefreshToken);

        return new TokenRefreshResponse(newAccessToken);
    }

    /**
     * [공통 로그아웃]
     * 로그인 토큰 쿠키의 수명을 즉시 만료시켜 브라우저에서 토큰을 깨끗이 날려버립니다.
     */
    @Override
    public void logout() {
        // Max-Age=0 혹은 Expires 과거 날짜로 쿠키 강제 파기
        jwtCookieManager.removeRefreshTokenCookie();
    }

    /**
     * [공통 프로필 조회]
     * 무상태성 Access Token을 전달받아 서명을 검증하고, 현재 인증 주체의 상세 정보를 리포트합니다.
     * Access Token 및 Refresh Token(쿠키 존재 시)의 만료 일시를 함께 응답합니다.
     */
    @Override
    public UserProfileResponse me(String authHeader) {
        // 1. Authorization: Bearer {token} 표준 포맷 검증
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException();
        }
        String token = authHeader.substring(7);

        // 2. 어세스 토큰의 해시 서명 검증
        if (!jwtProvider.validateToken(token)) {
            throw new UnauthorizedException();
        }

        // 3. 유효 토큰 내에 박힌 주체(Subject)의 Long ID 로드
        Long userId = jwtProvider.getUserId(token);

        // 4. Access Token 잔여 유효 시간 (현재 시점 기준, ms)
        long accessTokenExpiresIn = jwtProvider.getExpiresIn(token);

        // 5. Refresh Token 잔여 유효 시간 (쿠키 존재 시, 없으면 null)
        Long refreshTokenExpiresIn = jwtCookieManager.getRefreshTokenCookie()
                .filter(jwtProvider::validateToken)
                .map(jwtProvider::getExpiresIn)
                .orElse(null);

        // 6. 해당 회원 식별값으로 상세 유저 명세를 조회하여 반환
        AuthUserDetail user = authPort.loadUserById(userId);
        return new UserProfileResponse(
                user.getId(),
                user.getRole(),
                accessTokenExpiresIn,
                refreshTokenExpiresIn
        );
    }
}
