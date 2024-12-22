package org.example.bookreview.domain;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final Long id;
    private final String email;
    private final Map<String, Object> attributes;
    private final Collection<? extends GrantedAuthority> authorities;

    @Builder
    public CustomOAuth2User(Long id, String email, Set<Role> roles,
        Map<String, Object> attributes) {
        this.id = id;
        this.email = email;
        this.attributes = attributes;
        this.authorities = roles.stream()
            .map(role -> (GrantedAuthority) role::getKey)
            .collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return id.toString();
    }

    public Set<Role> getRoles() {
        return authorities.stream()
            .map(authority -> Role.valueOf(
                authority.getAuthority().replace("ROLE_", "")))
            .collect(Collectors.toSet());
    }
}