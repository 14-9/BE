package com.example.weluvwine.member.entity;

import com.example.weluvwine.jwt.refreshToken.RefreshToken;
import com.example.weluvwine.member.dto.SignupMemberRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String memberId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;


    public Member(String memberId, String password, String nickname) {
        this.memberId = memberId;
        this.password = password;
        this.nickname = nickname;
    }
}
