package com.example.lib.security.starter.internal.provider;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;

import com.example.lib.security.core.JwtProvider;
import com.example.lib.security.core.JwtValidationResult;
import com.example.lib.security.starter.internal.properties.SecurityProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT(JSON Web Token)의 생성, 파싱, 검증 및 관련 정밀 기능을 모두 수행하는 제네릭 마스터 구현체.
 *
 * @param <ID> 사용자 식별자(PK) 타입
 */
@Slf4j
public class DefaultJwtProvider<ID> implements JwtProvider<ID> {

    private final SecretKey key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;
    private final Function<String, ID> idParser;

    public DefaultJwtProvider(SecurityProperties securityProperties, Function<String, ID> idParser) {
        this.key = Keys.hmacShaKeyFor(securityProperties.jwtSecret().getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationTime = securityProperties.jwtExpirationTime();
        this.refreshTokenExpirationTime = securityProperties.jwtRefreshExpirationTime();
        this.idParser = idParser;
    }

    @Override
    public String createAccessToken(ID userId, String role) {
        return createAccessToken(userId, role, Map.of());
    }

    @Override
    public String createAccessToken(ID userId, String role, Map<String, Object> customClaims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpirationTime);

        var builder = Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiryDate);

        customClaims.forEach(builder::claim);

        return builder.signWith(key).compact();
    }

    @Override
    public String createRefreshToken(ID userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    @Override
    public ID getUserId(String token) {
        String subject = getClaims(token).getSubject();
        return subject != null ? idParser.apply(subject) : null;
    }

    @Override
    public ID getUserIdFromExpiredToken(String token) {
        try {
            return getUserId(token);
        } catch (ExpiredJwtException e) {
            String subject = e.getClaims().getSubject();
            return subject != null ? idParser.apply(subject) : null;
        }
    }

    @Override
    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    @Override
    public Object getClaim(String token, String claimName) {
        return getClaims(token).get(claimName);
    }

    @Override
    public <T> T getClaim(String token, String claimName, Class<T> type) {
        return getClaims(token).get(claimName, type);
    }

    @Override
    public long getExpiration(String token) {
        return getClaims(token).getExpiration().getTime();
    }

    @Override
    public boolean validateToken(String token) {
        return validateTokenDetail(token).isValid();
    }

    @Override
    public JwtValidationResult validateTokenDetail(String token) {
        try {
            getClaims(token);
            return JwtValidationResult.valid();
        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT token: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.EXPIRED, e.getMessage());
        } catch (SignatureException e) {
            log.debug("Invalid JWT signature: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.INVALID_SIGNATURE, e.getMessage());
        } catch (MalformedJwtException e) {
            log.debug("Malformed JWT token: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.MALFORMED, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.UNSUPPORTED, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.debug("JWT claims string is empty: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.EMPTY_CLAIMS, e.getMessage());
        } catch (Exception e) {
            log.debug("Unknown JWT validation error: {}", e.getMessage());
            return JwtValidationResult.invalid(JwtValidationResult.ValidationStatus.UNKNOWN, e.getMessage());
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
