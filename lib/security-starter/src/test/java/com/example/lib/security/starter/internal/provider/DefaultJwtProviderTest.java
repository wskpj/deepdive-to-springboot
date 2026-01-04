package com.example.lib.security.starter.internal.provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.lib.security.core.JwtValidationResult;
import com.example.lib.security.starter.internal.properties.SecurityProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

class DefaultJwtProviderTest {

    private static final String SECRET_KEY = "mysecretkeyfortestingpurposesonlywhichisatleast256bitslong";
    private static final String DIFFERENT_SECRET_KEY = "anothersecretkeyfortestingpurposesonlywhichisatleast256bitslong";
    private static final long ACCESS_EXPIRATION = 3600000L; // 1 hour
    private static final long REFRESH_EXPIRATION = 604800000L; // 7 days

    private DefaultJwtProvider<Long> jwtProvider;
    private SecurityProperties securityProperties;

    @BeforeEach
    void setUp() {
        securityProperties = new SecurityProperties(SECRET_KEY, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
        jwtProvider = new DefaultJwtProvider<>(securityProperties, Long::valueOf);
    }

    @Test
    @DisplayName("Access Token 생성 및 정상 복원 검증")
    void shouldCreateAndParseAccessToken() {
        // given
        Long userId = 12345L;
        String role = "ROLE_USER";
        Map<String, Object> customClaims = Map.of("email", "test@example.com", "isActive", true);

        // when
        String token = jwtProvider.createAccessToken(userId, role, customClaims);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtProvider.getUserId(token)).isEqualTo(userId);
        assertThat(jwtProvider.getRole(token)).isEqualTo(role);
        assertThat(jwtProvider.getClaim(token, "email")).isEqualTo("test@example.com");
        assertThat(jwtProvider.getClaim(token, "isActive", Boolean.class)).isTrue();
    }

    @Test
    @DisplayName("커스텀 클레임 없는 Access Token 생성 및 복원 검증")
    void shouldCreateAndParseAccessTokenWithoutCustomClaims() {
        // given
        Long userId = 999L;
        String role = "ROLE_ADMIN";

        // when
        String token = jwtProvider.createAccessToken(userId, role);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtProvider.getUserId(token)).isEqualTo(userId);
        assertThat(jwtProvider.getRole(token)).isEqualTo(role);
    }

    @Test
    @DisplayName("Refresh Token 생성 및 정상 복원 검증")
    void shouldCreateAndParseRefreshToken() {
        // given
        Long userId = 777L;

        // when
        String token = jwtProvider.createRefreshToken(userId);

        // then
        assertThat(token).isNotBlank();
        assertThat(jwtProvider.getUserId(token)).isEqualTo(userId);
        assertThat(jwtProvider.getRole(token)).isNull(); // Refresh Token은 role 클레임이 없음
    }

    @Test
    @DisplayName("정상적인 토큰에 대한 유효성 검증 성공")
    void shouldValidateCorrectToken() {
        // given
        String token = jwtProvider.createAccessToken(1L, "ROLE_USER");

        // when & then
        assertThat(jwtProvider.validateToken(token)).isTrue();
        JwtValidationResult result = jwtProvider.validateTokenDetail(token);
        assertThat(result.isValid()).isTrue();
        assertThat(result.status()).isEqualTo(JwtValidationResult.ValidationStatus.VALID);
        assertThat(result.message()).isEqualTo("Valid token");
    }

    @Test
    @DisplayName("만료된 토큰 검증 시 EXPIRED 반환 및 getUserIdFromExpiredToken 구동 검증")
    void shouldHandleExpiredToken() throws InterruptedException {
        // given: 1ms의 아주 짧은 만료 시간을 갖는 프로바이더 생성
        SecurityProperties shortProperties = new SecurityProperties(SECRET_KEY, 1L, 1L);
        DefaultJwtProvider<Long> shortProvider = new DefaultJwtProvider<>(shortProperties, Long::valueOf);

        String expiredToken = shortProvider.createAccessToken(555L, "ROLE_USER");
        Thread.sleep(5); // 만료를 위한 대기

        // when: 만료 토큰 유효성 검증
        boolean isValid = shortProvider.validateToken(expiredToken);
        JwtValidationResult result = shortProvider.validateTokenDetail(expiredToken);

        // then
        assertThat(isValid).isFalse();
        assertThat(result.isValid()).isFalse();
        assertThat(result.status()).isEqualTo(JwtValidationResult.ValidationStatus.EXPIRED);
        assertThat(result.message()).contains("JWT expired");

        // expired token이어도 getUserIdFromExpiredToken은 정상적으로 ID를 구해야 함
        assertThat(shortProvider.getUserIdFromExpiredToken(expiredToken)).isEqualTo(555L);
    }

    @Test
    @DisplayName("서명이 일치하지 않는 토큰 검증 시 INVALID_SIGNATURE 반환 및 복원 거부 검증")
    void shouldHandleInvalidSignatureToken() {
        // given: 다른 시크릿 키를 갖는 프로바이더로 발급된 토큰 준비
        SecurityProperties otherProperties = new SecurityProperties(DIFFERENT_SECRET_KEY, ACCESS_EXPIRATION, REFRESH_EXPIRATION);
        DefaultJwtProvider<Long> otherProvider = new DefaultJwtProvider<>(otherProperties, Long::valueOf);
        String invalidSignToken = otherProvider.createAccessToken(123L, "ROLE_USER");

        // when: 본래의 프로바이더로 검증 수행
        boolean isValid = jwtProvider.validateToken(invalidSignToken);
        JwtValidationResult result = jwtProvider.validateTokenDetail(invalidSignToken);

        // then
        assertThat(isValid).isFalse();
        assertThat(result.isValid()).isFalse();
        assertThat(result.status()).isEqualTo(JwtValidationResult.ValidationStatus.INVALID_SIGNATURE);

        // 서명이 다른 토큰을 복원하려고 시도할 때 예외 발생 검증 (위조 위협 방어)
        assertThatThrownBy(() -> jwtProvider.getUserIdFromExpiredToken(invalidSignToken))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    @DisplayName("손상된 토큰 검증 시 MALFORMED 반환 및 복원 거부 검증")
    void shouldHandleMalformedToken() {
        // given
        String malformedToken = "this.is.a.malformed.token.string";

        // when
        boolean isValid = jwtProvider.validateToken(malformedToken);
        JwtValidationResult result = jwtProvider.validateTokenDetail(malformedToken);

        // then
        assertThat(isValid).isFalse();
        assertThat(result.isValid()).isFalse();
        assertThat(result.status()).isEqualTo(JwtValidationResult.ValidationStatus.MALFORMED);

        // 깨진 토큰 복원 시도 시 예외 발생 검증
        assertThatThrownBy(() -> jwtProvider.getUserIdFromExpiredToken(malformedToken))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("서명이 없는(Unsigned) 토큰에 대해 UNSUPPORTED 반환 검증")
    void shouldHandleUnsupportedToken() {
        // given: 서명이 없는 일반 토큰 생성
        String unsignedToken = Jwts.builder()
                .subject("123")
                .compact();

        // when
        boolean isValid = jwtProvider.validateToken(unsignedToken);
        JwtValidationResult result = jwtProvider.validateTokenDetail(unsignedToken);

        // then
        assertThat(isValid).isFalse();
        assertThat(result.isValid()).isFalse();
        assertThat(result.status()).isEqualTo(JwtValidationResult.ValidationStatus.UNSUPPORTED);

        // 복원 시도 시 예외 발생 검증
        assertThatThrownBy(() -> jwtProvider.getUserIdFromExpiredToken(unsignedToken))
                .isInstanceOf(UnsupportedJwtException.class);
    }

    @Test
    @DisplayName("토큰 문자열이 비어있거나 null인 경우 EMPTY_CLAIMS 반환 검증")
    void shouldHandleEmptyOrNullToken() {
        // given
        String emptyToken = "";
        String nullToken = null;

        // when & then
        assertThat(jwtProvider.validateToken(emptyToken)).isFalse();
        assertThat(jwtProvider.validateTokenDetail(emptyToken).status())
                .isEqualTo(JwtValidationResult.ValidationStatus.EMPTY_CLAIMS);

        assertThat(jwtProvider.validateToken(nullToken)).isFalse();
        assertThat(jwtProvider.validateTokenDetail(nullToken).status())
                .isEqualTo(JwtValidationResult.ValidationStatus.EMPTY_CLAIMS);
    }
}
