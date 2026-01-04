package com.example.lib.common.core.context;

import java.util.Optional;

/**
 * 현재 실행 컨텍스트의 인증 주체(Principal) 정보에 접근하기 위한 인터페이스.
 * 비즈니스 모듈이 특정 보안 프레임워크(예: Spring Security)에 직접 의존하는 것을 방지합니다.
 *
 * @param <ID> 사용자 고유 식별자(ID)의 타입 (예: Long, UUID, String)
 */
public interface PrincipalContext<ID> {

    /**
     * 현재 인증된 사용자의 고유 식별자(ID)를 반환합니다.
     * 
     * @return 사용자 ID (인증되지 않은 경우 empty)
     */
    Optional<ID> getCurrentUserId();

    /**
     * 현재 인증된 사용자의 역할(Role)을 반환합니다.
     * 
     * @return 사용자 역할 (예: CUSTOMER, OWNER)
     */
    Optional<String> getCurrentUserRole();

    /**
     * 현재 사용자가 인증된 상태인지 확인합니다.
     */
    default boolean isAuthenticated() {
        return getCurrentUserId().isPresent();
    }
}
