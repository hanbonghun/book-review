package org.example.bookreview.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.exception.InvalidTokenException;
import org.example.bookreview.common.exception.TokenExpiredException;
import org.example.bookreview.config.JwtProperties;
import org.example.bookreview.domain.Role;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(
        String id,
        String email,
        Set<Role> roles,
        Date now,
        TokenType tokenType
    ) {
        long expirationTime = getExpirationTimeForTokenType(tokenType);

        JwtBuilder builder = Jwts.builder()
            .setSubject(id)
            .claim("email", email)
            .claim("roles", roles.stream()
                .map(Role::getKey)
                .collect(Collectors.toSet()))
            .claim("tokenType", tokenType.name())
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + expirationTime))
            .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()));

        return builder.compact();
    }

    public String regenerateAccessToken(String refreshToken, Date now) {
        validateToken(refreshToken);

        String userId = getUserId(refreshToken);
        String email = getEmail(refreshToken);
        Set<Role> role = getRoles(refreshToken);

        return generateToken(userId, email, role, now, TokenType.ACCESS);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("만료된 토큰입니다.");
        } catch (SecurityException | MalformedJwtException e) {
            throw new InvalidTokenException("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("JWT 클레임 문자열이 비어있습니다.");
        }

        return true;
    }

    public String getUserId(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public long getTokenExpirationTime(String token) {
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime();
    }

    public long getRemainingValidityTime(String token) {
        long expirationTime = getTokenExpirationTime(token);
        long currentTime = System.currentTimeMillis();
        return expirationTime - currentTime;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private long getExpirationTimeForTokenType(TokenType tokenType) {
        return tokenType == TokenType.ACCESS ?
            jwtProperties.getAccessTokenExpiration() :
            jwtProperties.getRefreshTokenExpiration();
    }

    public String getEmail(String token) {
        Claims claims = parseClaims(token);
        return claims.get("email", String.class);
    }

    public Set<Role> getRoles(String token) {
        Claims claims = parseClaims(token);
        List<String> roleStrings = claims.get("roles", List.class);
        return roleStrings.stream()
            .map(roleString -> Role.valueOf(roleString.replace("ROLE_", "")))  // ROLE_ 접두사 제거
            .collect(Collectors.toSet());
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenInfo {

        private String accessToken;
        private String refreshToken;
    }

    public enum TokenType {
        ACCESS, REFRESH
    }
}