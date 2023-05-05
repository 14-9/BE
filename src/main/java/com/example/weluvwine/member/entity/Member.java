package com.example.weluvwine.member.entity;

import com.example.weluvwine.member.dto.SignupMemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column
    private String memberId;

    @Column
    private String password;

    @Column
    private String nickname;


    public Member(SignupMemberRequestDto requestDto) {
        this.memberId = requestDto.getMemberId();
        this.password = requestDto.getPassword();
        this.nickname = requestDto.getNickname();
    }
}
