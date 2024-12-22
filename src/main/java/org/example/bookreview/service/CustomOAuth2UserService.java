package org.example.bookreview.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.domain.CustomOAuth2User;
import org.example.bookreview.domain.Member;
import org.example.bookreview.domain.Member.Role;
import org.example.bookreview.repository.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    private static final String KAKAO_ACCOUNT_KEY = "kakao_account";
    private static final String PROPERTIES_KEY = "properties";
    private static final String EMAIL_KEY = "email";
    private static final String NICKNAME_KEY = "nickname";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = String.valueOf(attributes.get("id")); // Provider ID
        String email = getNestedValue(attributes, KAKAO_ACCOUNT_KEY, EMAIL_KEY);
        String name = getNestedValue(attributes, PROPERTIES_KEY, NICKNAME_KEY, "Unknown");

        log.debug("provider = {}", provider);
        log.debug("providerId = {}", providerId);
        log.debug("email = {}", email);
        log.debug("name = {}", name);

        Member member = memberRepository.findByEmail(email)
            .orElseGet(() -> createNewMember(email, name, provider, providerId));

        return new CustomOAuth2User(member, attributes);
    }

    private Member createNewMember(String email, String name, String provider, String providerId) {
        Member newMember = Member.builder()
            .email(email)
            .name(name)
            .provider(provider)
            .providerId(providerId)
            .role(Role.USER)
            .build();
        memberRepository.save(newMember);
        return newMember;
    }

    private String getNestedValue(Map<String, Object> attributes, String key, String nestedKey) {
        return getNestedValue(attributes, key, nestedKey, null);
    }

    private String getNestedValue(Map<String, Object> attributes, String key, String nestedKey, String defaultValue) {
        Object nestedMap = attributes.get(key);
        if (nestedMap instanceof Map) {
            Object value = ((Map<String, Object>) nestedMap).get(nestedKey);
            return value != null ? value.toString() : defaultValue;
        }
        return defaultValue;
    }
}