package com.example.weluvwine.member.repository;

import com.example.weluvwine.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByMemberId(String memberId);
}
