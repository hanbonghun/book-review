package org.example.bookreview.member.service;

import lombok.RequiredArgsConstructor;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.dto.MyInfoResponse;
import org.example.bookreview.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MyInfoResponse getMyInfo(Long userId) {
        Member member = memberRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorType.USER_NOT_FOUND));

        return new MyInfoResponse(
            member.getId(),
            member.getName(),
            member.getEmail(),
            member.getRoles(),
            member.getProvider()
        );
    }
}
