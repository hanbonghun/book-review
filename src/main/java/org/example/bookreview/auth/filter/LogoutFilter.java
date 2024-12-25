package org.example.bookreview.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.auth.repository.TokenRepository;
import org.example.bookreview.auth.service.CookieHandler;
import org.example.bookreview.auth.service.JwtTokenProvider;
import org.example.bookreview.auth.service.TokenExtractor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;
    private final CookieHandler cookieHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) {

        String refreshToken = tokenExtractor.extractRefreshToken(request);
        if (!isValidRefreshToken(refreshToken)) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        blacklistRefreshToken(refreshToken);
        cookieHandler.clearRefreshTokenCookie(response);
        response.setStatus(HttpStatus.OK.value());
    }

    private boolean isValidRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            return false;
        }
        long remainingTime = jwtTokenProvider.getRemainingValidityTime(refreshToken);
        return remainingTime > 0;
    }

    private void blacklistRefreshToken(String refreshToken) {
        long remainingTime = jwtTokenProvider.getRemainingValidityTime(refreshToken);
        tokenRepository.addToBlacklist(refreshToken, remainingTime);
        log.debug("Refresh Token has been blacklisted");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return !("POST".equals(method) && uri.endsWith("/logout"));
    }
}