package com.example.weluvwine.member.dto;

import lombok.Getter;

import javax.validation.constraints.Pattern;


@Getter
public class SignupMemberRequestDto {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$", message = "5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.")
    private String memberId;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$",
            message = "8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    private String nickname;

}
