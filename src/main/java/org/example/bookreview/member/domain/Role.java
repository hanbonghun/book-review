package org.example.bookreview.member.domain;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    GUEST("ROLE_GUEST");

    private final String key;

    Role(String key) {
        this.key = key;
    }
}