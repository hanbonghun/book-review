package org.example.bookreview.member.controller;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.member.dto.MyInfoResponse;
import org.example.bookreview.member.service.MemberService;
import org.example.bookreview.oauth.CustomOAuth2User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/members")
@RequiredArgsConstructor
@RequestMapping("/api/members")

public class MemberController {

    private final MemberService memberService;

    @GetMapping("/me")
    public MyInfoResponse getMyInfo(@AuthenticationPrincipal CustomOAuth2User customOAuth2User) {
        return memberService.getMyInfo(customOAuth2User.getId());
    }
}
