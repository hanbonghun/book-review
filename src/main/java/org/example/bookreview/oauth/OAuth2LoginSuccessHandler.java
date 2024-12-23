package org.example.bookreview.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.auth.service.JwtTokenProvider;
import org.example.bookreview.config.JwtProperties;
import org.example.bookreview.auth.repository.TokenRepository;
import org.example.bookreview.auth.service.JwtTokenProvider.TokenInfo;
import org.example.bookreview.auth.service.JwtTokenProvider.TokenType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final SecurityContextRepository securityContextRepository;
    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final String clientRedirectUri = "http://localhost:5174"; // 프론트엔드 도메인

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        saveAuthenticationContext(authentication, request, response);

        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        TokenInfo tokenInfo = generateTokens(oAuth2User);

        saveRefreshToken(oAuth2User.getId().toString(), tokenInfo.getRefreshToken());
        setRefreshTokenCookie(response, tokenInfo.getRefreshToken());
        // 프론트엔드로 리다이렉트하면서 액세스 토큰을 쿼리 파라미터로 전달
        String redirectUrl = UriComponentsBuilder.fromUriString(clientRedirectUri)
            .queryParam("accessToken", tokenInfo.getAccessToken())
            .build().toUriString();

        response.sendRedirect(redirectUrl);    }

    private void saveAuthenticationContext(Authentication authentication,
        HttpServletRequest request, HttpServletResponse response) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
    }

    private TokenInfo generateTokens(CustomOAuth2User oAuth2User) {
        Date now = new Date();

        String accessToken = tokenProvider.generateToken(
            oAuth2User.getId().toString(),
            oAuth2User.getEmail(),
            oAuth2User.getRoles(),
            now,
            TokenType.ACCESS
        );

        String refreshToken = tokenProvider.generateToken(
            oAuth2User.getId().toString(),
            oAuth2User.getEmail(),
            oAuth2User.getRoles(),
            now,
            TokenType.REFRESH
        );

        return new TokenInfo(accessToken, refreshToken);
    }

    private void saveRefreshToken(String userId, String refreshToken) {
        tokenRepository.saveRefreshToken(
            userId,
            refreshToken,
            jwtProperties.getRefreshTokenExpiration()
        );
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie refreshTokenCookie = ResponseCookie.from(
                jwtProperties.getRefreshTokenCookieName(),
                refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("Strict")
            .path("/")
            .maxAge(Duration.ofMillis(jwtProperties.getRefreshTokenExpiration()))
            .build();

        response.setHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    private void writeAccessTokenResponse(HttpServletResponse response, String accessToken)
        throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> responseBody = Map.of(
            "accessToken", accessToken
        );

        objectMapper.writeValue(response.getWriter(), responseBody);
    }
}