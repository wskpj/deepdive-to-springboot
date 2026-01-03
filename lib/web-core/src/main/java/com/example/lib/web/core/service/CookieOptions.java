package com.example.lib.web.core.service;

import lombok.Builder;
import lombok.Getter;

/**
 * Cookie 생성을 위한 세부 옵션을 설정하는 클래스
 */
@Getter
@Builder
public class CookieOptions {
    
    @Builder.Default
    private final String path = "/";
    
    private final String domain;
    
    @Builder.Default
    private final boolean httpOnly = true;
    
    @Builder.Default
    private final boolean secure = true;
    
    @Builder.Default
    private final String sameSite = "Lax";

    public static CookieOptions defaultOptions() {
        return CookieOptions.builder().build();
    }
}
