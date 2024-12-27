package org.example.bookreview.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(provider, oAuth2User.getAttributes());

        Member member = memberService.findOrCreateMember(attributes.getEmail(),
            attributes.getName(), provider, attributes.getProviderId());

        return new CustomOAuth2User(
            member.getId(),
            attributes.getEmail(),
            member.getRoles(),
            oAuth2User.getAttributes()
        );
    }
}