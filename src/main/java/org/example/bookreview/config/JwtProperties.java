package org.example.bookreview.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@Component
@Getter
@RequiredArgsConstructor
public class JwtProperties {
    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration = 1000L * 60 * 30;
    
    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7;
    
    private final String refreshTokenCookieName = "refresh_token";
}
