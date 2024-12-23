package org.example.bookreview.auth.service;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.member.domain.Role;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;

    public Authentication getAuthentication(String token) {
        String id = jwtTokenProvider.getUserId(token);
        String email = jwtTokenProvider.getEmail(token);
        Set<Role> roles = jwtTokenProvider.getRoles(token);

        CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
            .id(Long.valueOf(id))
            .email(email)
            .roles(roles)
            .attributes(Collections.emptyMap())
            .build();

        return new UsernamePasswordAuthenticationToken(customOAuth2User, null,
            customOAuth2User.getAuthorities());
    }
}