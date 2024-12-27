package org.example.bookreview.utils;

import java.util.Collections;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.domain.Role;
import org.springframework.test.util.ReflectionTestUtils;

public class TestEntityFactory {

    public static Member createMember(Long id, String email, String name, String provider) {
        Member member = Member.builder()
            .email(email)
            .name(name)
            .provider(provider)
            .roles(Collections.singleton(Role.USER))
            .build();

        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

    public static Member createDefaultMember(Long id) {
        return createMember(id, "test@example.com", "테스터", "KAKAO");
    }
}