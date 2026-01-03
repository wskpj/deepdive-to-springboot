package com.example.lib.security.core;

/**
 * JWT 유효성 검증 결과를 정밀하게 담는 불변 레코드입니다.
 */
public record JwtValidationResult(
    boolean isValid,
    ValidationStatus status,
    String message
) {
    public enum ValidationStatus {
        VALID,              // 유효함
        EXPIRED,            // 만료됨
        MALFORMED,          // 구조 손상
        INVALID_SIGNATURE,  // 서명 위조/불일치
        UNSUPPORTED,        // 지원하지 않는 구조
        EMPTY_CLAIMS,       // 클레임이 비어있음
        UNKNOWN             // 기타 알 수 없는 에러
    }

    public static JwtValidationResult valid() {
        return new JwtValidationResult(true, ValidationStatus.VALID, "Valid token");
    }

    public static JwtValidationResult invalid(ValidationStatus status, String message) {
        return new JwtValidationResult(false, status, message);
    }
}
