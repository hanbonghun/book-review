package org.example.bookreview.member.repository;

import java.util.Optional;
import org.example.bookreview.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}
