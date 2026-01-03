package com.example.lib.auth.starter.internal.port;

/**
 * 회원 인증 및 토큰 생성 시 요구되는 최소한의 유저 명세를 추상화한 인터페이스.
 * 헥사고날 아키텍처 상의 도메인 결합도를 낮추기 위해, 이메일/이름 등 도메인 고유 필드를 배제하고
 * 오직 토큰 기반 인가를 충족하기 위한 회원 식별값(ID)과 역할(Role)만을 갖도록 설계되었습니다.
 */
public interface AuthUserDetail {

    /**
     * 회원의 데이터베이스 고유 식별값(Long ID)을 반환합니다.
     * JWT Access Token의 Subject 필드로 저장됩니다.
     */
    Long getId();

    /**
     * 회원의 보안 등급/권한 역할명(예: CUSTOMER, ADMIN)을 반환합니다.
     * JWT Access Token의 claims 내 "role"로 바인딩됩니다.
     */
    String getRole();
}
