package com.example.lib.security.principal.internal.context;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.lib.common.core.context.PrincipalContext;

/**
 * Spring Security의 SecurityContextHolder를 사용하는 PrincipalContext 구현체.
 */
public class SpringSecurityPrincipalContext<ID> implements PrincipalContext<ID> {

    private final Class<ID> idType;

    public SpringSecurityPrincipalContext(Class<ID> idType) {
        this.idType = idType;
    }

    @Override
    public Optional<ID> getCurrentUserId() {
        return getAuthentication()
                .map(Authentication::getPrincipal)
                .filter(idType::isInstance)
                .map(idType::cast);
    }

    @Override
    public Optional<String> getCurrentUserRole() {
        return getAuthentication()
                .map(auth -> auth.getAuthorities().stream()
                        .map(grantedAuthority -> grantedAuthority.getAuthority())
                        .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                        .findFirst()
                        .orElse(null));
    }

    private Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .filter(Authentication::isAuthenticated);
    }
}
