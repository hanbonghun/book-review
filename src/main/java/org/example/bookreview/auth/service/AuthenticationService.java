package org.example.bookreview.auth.service;

import java.util.Collections;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.example.bookreview.member.domain.Role;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public Authentication getAuthentication(String id, String email, Set<Role> roles) {
        CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
            .id(Long.valueOf(id))
            .email(email)
            .roles(roles)
            .attributes(Collections.emptyMap())
            .build();

        return new UsernamePasswordAuthenticationToken(
            customOAuth2User,
            null,
            customOAuth2User.getAuthorities()
        );
    }

    public Authentication getGuestAuthentication() {
        CustomOAuth2User customOAuth2User = CustomOAuth2User.builder()
            .id(-1L)  // 게스트 사용자 아이디
            .email("guest@invalid")
            .roles(Collections.singleton(Role.GUEST))  // 게스트 역할
            .attributes(Collections.emptyMap())
            .build();

        return new UsernamePasswordAuthenticationToken(
            customOAuth2User,
            null,
            customOAuth2User.getAuthorities()
        );
    }
}