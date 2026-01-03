package com.example.lib.auth.starter.internal.port;

/**
 * 회원/인증 기능(auth-starter)과 비즈니스/데이터베이스 레이어 간의 디커플링을 위한 SPI 포트 인터페이스.
 * 헥사고날(Hexagonal) 아키텍처의 포트(Port)에 해당하며, 실제 애플리케이션 코드가 이 포트를 구현(Implement)하여 아웃바운드 어댑터 역할을 수행해야 합니다.
 * 도메인 종속성을 피하기 위해 식별자 필드를 'username'으로 추상화하여 설계되었습니다.
 */
public interface AuthPort {

    /**
     * 로그인 자격 증명을 검증하고 회원 상세 정보를 획득합니다.
     *
     * @param username 로그인 고유 식별자 (이메일, 로그인 ID 등)
     * @param password 평문 패스워드
     * @return 로그인 성공 시 반환되는 추상 회원 정보 (ID, Role 포함)
     */
    AuthUserDetail login(String username, String password);

    /**
     * 고유 회원 식별값(ID)을 기준으로 유저 프로필 상세를 로드합니다.
     * 토큰 재발급(RTR), 프로필 조회(/me) 등 인증 주체 조회를 처리할 때 사용됩니다.
     *
     * @param userId 회원 고유 식별값(Long ID)
     * @return 회원의 추상 정보(ID, Role 포함) 및 토큰 정보(만료 시간) DTO
     */
    AuthUserDetail loadUserById(Long userId);
}
