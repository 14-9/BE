package com.example.weluvwine.domain.member.repository;


import com.example.weluvwine.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByMemberId(String memberId);

//    Optional<Member> findByEmail(String email);
}
