package com.example.lib.web.starter.internal.filter;

import java.util.Arrays;
import org.springframework.core.MethodParameter;

/**
 * StandardResponseHandler가 응답을 ApiResult로 래핑할지 여부를 결정하는 필터 인터페이스
 * 주로 특정 패키지 경로(Prefix)에 대해서만 래핑을 적용하도록 필터링하는 데 사용
 */
@FunctionalInterface
public interface ResponseFilter {

    boolean supports(MethodParameter returnType);

    static ResponseFilter ofPrefixes(String... prefixes) {
        return returnType -> {
            String packageName = returnType.getDeclaringClass().getPackageName();
            return Arrays.stream(prefixes).anyMatch(packageName::startsWith);
        };
    }
}
