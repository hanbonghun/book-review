package org.example.bookreview.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.auth.service.AuthenticationService;
import org.example.bookreview.auth.service.JwtTokenProvider;
import org.example.bookreview.auth.service.TokenExtractor;
import org.example.bookreview.member.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenExtractor tokenExtractor;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = tokenExtractor.extractAccessToken(request);
            if (token != null) {
                jwtTokenProvider.validateToken(token);
                String id = jwtTokenProvider.getUserId(token);
                String email = jwtTokenProvider.getEmail(token);
                Set<Role> roles = jwtTokenProvider.getRoles(token);

                Authentication authentication = authenticationService.getAuthentication(id, email, roles);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.getContext().setAuthentication(authenticationService.getGuestAuthentication());
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/login") || path.equals("/logout");
    }
}