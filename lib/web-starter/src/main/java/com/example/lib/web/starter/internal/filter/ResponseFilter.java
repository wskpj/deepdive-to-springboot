package com.example.lib.web.starter.internal.filter;

import java.util.Arrays;
import org.springframework.core.MethodParameter;

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
