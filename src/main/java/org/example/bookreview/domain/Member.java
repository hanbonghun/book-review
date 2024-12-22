package org.example.bookreview.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;        // google, naver 등 OAuth2 제공자
    private String providerId;      // OAuth2 제공자의 사용자 식별자


    @Builder
    public Member(String email, String name, Role role, String provider, String providerId) {
        this.email = email;
        this.name = name;
        this.role = role == null ? Role.USER : role;  // 기본 역할은 USER
        this.provider = provider;
        this.providerId = providerId;
    }

    public enum Role {
        USER, ADMIN
    }
}
