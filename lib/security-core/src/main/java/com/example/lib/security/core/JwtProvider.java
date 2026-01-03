package com.example.lib.security.core;

import java.util.Map;

/**
 * JWT(JSON Web Token)의 생성, 파싱, 검증을 담당하는 컴포넌트 인터페이스
 *
 * @param <ID> 사용자 식별자(PK) 타입
 */
public interface JwtProvider<ID> {

    /**
     * 기본 클레임(role 등)을 포함한 Access Token을 생성합니다.
     *
     * @param userId 사용자 식별자 (Subject)
     * @param role   사용자 역할
     * @return 생성된 Access Token
     */
    String createAccessToken(ID userId, String role);

    /**
     * 사용자 식별자, 역할 및 추가 커스텀 클레임을 포함하는 Access Token을 생성합니다.
     *
     * @param userId       사용자 식별자 (Subject)
     * @param role         사용자 역할
     * @param customClaims 추가적인 커스텀 클레임 맵
     * @return 생성된 Access Token
     */
    String createAccessToken(ID userId, String role, Map<String, Object> customClaims);

    /**
     * Refresh Token을 생성합니다. 보안상 최소한의 정보(Subject 등)만 담는 것을 권장합니다.
     *
     * @param userId 사용자 식별자 (Subject)
     * @return 생성된 Refresh Token
     */
    String createRefreshToken(ID userId);

    /**
     * 토큰에서 사용자 식별자(Subject)를 안전하게 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 식별자 (ID)
     */
    ID getUserId(String token);

    /**
     * 만료된 토큰인 경우에도 예외를 발생시키지 않고 사용자 식별자(Subject)를 추출합니다.
     * (만료된 Access Token을 기반으로 Refresh Token 검증을 진행하는 흐름 등에서 유용하게 사용됩니다.)
     *
     * @param token JWT 토큰
     * @return 사용자 식별자 (ID)
     */
    ID getUserIdFromExpiredToken(String token);

    /**
     * 토큰에서 역할(Role) 정보를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 역할 문자열
     */
    String getRole(String token);

    /**
     * 토큰에서 특정 커스텀 클레임 값을 추출합니다.
     *
     * @param token JWT 토큰
     * @param claimName 클레임 키 이름
     * @return 클레임 값
     */
    Object getClaim(String token, String claimName);

    /**
     * 토큰에서 특정 커스텀 클레임을 특정 타입으로 안전하게 추출합니다.
     *
     * @param token JWT 토큰
     * @param claimName 클레임 키 이름
     * @param type 기대하는 반환 타입 클래스
     * @return 타입 변환된 클레임 값
     */
    <T> T getClaim(String token, String claimName, Class<T> type);

    /**
     * 토큰의 서명, 포맷, 만료 여부 등 모든 유효성을 검증합니다.
     *
     * @param token JWT 토큰
     * @return 유효 여부
     */
    boolean validateToken(String token);

    /**
     * 토큰의 유효성 검증 후 상세 상태 정보를 반환합니다.
     *
     * @param token JWT 토큰
     * @return 검증 상세 결과 객체 (JwtValidationResult)
     */
    JwtValidationResult validateTokenDetail(String token);

    /**
     * 하위 호환성을 제공하기 위한 기본 메서드입니다. 기존의 createToken 호출을 createAccessToken으로 위임합니다.
     *
     * @param userId 사용자 식별자
     * @param role 사용자 역할
     * @return 생성된 Access Token
     */
    default String createToken(ID userId, String role) {
        return createAccessToken(userId, role);
    }
}
