package org.example.bookreview.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import org.example.bookreview.common.error.ErrorType;
import org.example.bookreview.common.exception.BusinessException;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.domain.Role;
import org.example.bookreview.member.dto.MyInfoResponse;
import org.example.bookreview.member.repository.MemberRepository;
import org.example.bookreview.utils.TestEntityFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    void 존재하는_사용자의_정보를_조회할_수_있다() {
        // given
        Long userId = 1L;
        Member member = TestEntityFactory.createDefaultMember(userId);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // when
        MyInfoResponse response = memberService.getMyInfo(userId);

        // then
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getEmail()).isEqualTo("test@example.com");
        assertThat(response.getName()).isEqualTo("테스터");
        assertThat(response.getProvider()).isEqualTo("KAKAO");
        assertThat(response.getRoles()).containsExactly(Role.USER);
    }

    @Test
    void 존재하지_않는_사용자_조회시_예외가_발생한다() {
        // given
        Long userId = 999L;
        when(memberRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getMyInfo(userId))
            .isInstanceOf(BusinessException.class)
            .hasFieldOrPropertyWithValue("errorType", ErrorType.USER_NOT_FOUND);
    }

    @Test
    void 기존_회원은_그대로_반환한다() {
        // given
        String email = "exist@example.com";
        Member existingMember = Member.builder()
            .email(email)
            .name("기존회원")
            .provider("KAKAO")
            .roles(Collections.singleton(Role.USER))
            .build();

        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(existingMember));

        // when
        Member result = memberService.findOrCreateMember(email, "신규이름", "KAKAO", "providerId");

        // then
        assertThat(result).isEqualTo(existingMember);
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void 신규_회원은_저장_후_반환한다() {
        // given
        String email = "new@example.com";
        String name = "신규회원";
        String provider = "KAKAO";
        String providerId = "123456";

        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> {
            Member memberToSave = invocation.getArgument(0);
            return Member.builder()
                .email(memberToSave.getEmail())
                .name(memberToSave.getName())
                .provider(memberToSave.getProvider())
                .providerId(memberToSave.getProviderId())
                .roles(memberToSave.getRoles())
                .build();
        });

        // when
        Member result = memberService.findOrCreateMember(email, name, provider, providerId);

        // then
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getProvider()).isEqualTo(provider);
        assertThat(result.getProviderId()).isEqualTo(providerId);
        assertThat(result.getRoles()).containsExactly(Role.USER);

        verify(memberRepository).save(any(Member.class));
    }
}