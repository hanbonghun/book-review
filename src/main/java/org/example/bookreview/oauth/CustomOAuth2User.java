package org.example.bookreview.oauth;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.bookreview.member.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomOAuth2User implements OAuth2User {

    private Long id;
    private String email;
    private Map<String, Object> attributes;
    private Collection<? extends GrantedAuthority> authorities;

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