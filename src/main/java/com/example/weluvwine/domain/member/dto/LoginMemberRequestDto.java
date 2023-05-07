package com.example.weluvwine.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginMemberRequestDto {
    private String memberId;
    private String password;
}
