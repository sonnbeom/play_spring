package com.example.play.member.repository;

import com.example.play.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);
    Member findBySocialUserId(String socialUserId);
}
