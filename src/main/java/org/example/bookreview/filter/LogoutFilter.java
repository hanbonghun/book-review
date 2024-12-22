package org.example.bookreview.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.repository.TokenRepository;
import org.example.bookreview.service.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) {

        String refreshToken = jwtTokenProvider.extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        long remainingTime = jwtTokenProvider.getRemainingValidityTime(refreshToken);
        if (remainingTime <= 0) {
            response.setStatus(HttpStatus.OK.value());
            return;
        }

        tokenRepository.addToBlacklist(refreshToken, remainingTime);
        log.debug("Refresh Token has been blacklisted");

        clearRefreshTokenCookie(response);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        return !("POST".equals(method) && uri.endsWith("/logout"));
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}