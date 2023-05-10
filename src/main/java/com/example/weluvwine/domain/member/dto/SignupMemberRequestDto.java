package com.example.weluvwine.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
public class SignupMemberRequestDto {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$", message = "아이디는 5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.")
    private String memberId;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$",
            message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @Size(min = 2, max = 8, message = "닉네임은 2~8글자로 생성해주세요")
    private String nickname;

//    private String email;

}
