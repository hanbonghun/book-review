package org.example.bookreview.member.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import org.example.bookreview.auth.service.JwtTokenProvider;
import org.example.bookreview.auth.service.JwtTokenProvider.TokenType;
import org.example.bookreview.member.domain.Member;
import org.example.bookreview.member.domain.Role;
import org.example.bookreview.member.repository.MemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")  // 추가
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Long memberId;

    @BeforeEach
    void setUp() {
        Member testMember = Member.builder()
            .email("test@example.com")
            .name("testUser")
            .roles(Set.of(Role.USER))
            .provider("KAKAO")
            .build();

        memberId = memberRepository.save(testMember).getId();
    }

    @Test
    public void 유효한_토큰으로_내_정보를_조회할_수_있다() throws Exception {
        // given
        String token = jwtTokenProvider.generateToken(
            String.valueOf(memberId),
            "test@example.com",
            Set.of(Role.USER),
            Date.from(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()),
            TokenType.ACCESS
        );

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/members/me")
            .header("Authorization", "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON));

        System.out.println("resultActions = " + resultActions);
        // then
        resultActions
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(memberId))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.name").value("testUser"))
            .andExpect(jsonPath("$.roles").isArray())
            .andExpect(jsonPath("$.roles[0]").value("USER"))
            .andDo(document("member-get-my-info",
                resource(
                    ResourceSnippetParameters.builder()
                        .tag("Member API")
                        .summary("내 정보 조회")
                        .description("현재 로그인한 사용자의 정보를 조회합니다")
                        .requestHeaders(
                            headerWithName("Authorization").description("Bearer 인증 토큰")
                        )
                        .responseFields(
                            fieldWithPath("id").description("사용자 ID"),
                            fieldWithPath("email").description("사용자 이메일"),
                            fieldWithPath("name").description("사용자 이름"),
                            fieldWithPath("roles").description("사용자 권한 목록"),
                            fieldWithPath("provider").description("인증 제공자(KAKAO 등)")
                        )
                        .build()
                )));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
        memberRepository.deleteById(memberId);
    }
}