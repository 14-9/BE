package com.example.weluvwine.domain.member.controller;

import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.dto.VerifyEmailRequestDto;
import com.example.weluvwine.domain.member.service.EmailServiceImpl;
import com.example.weluvwine.domain.member.service.EmailVerifiedService;
import com.example.weluvwine.domain.member.service.MemberService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine/user")
@Tag(name = "MemberController", description = "유저 관련 API")
public class MemberController {
    private final MemberService memberService;
    private final EmailVerifiedService emailVerifiedService;


    @Operation(summary = "회원가입 메서드", description = "회원가입 메서드입니다.")
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupMemberRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    @Operation(summary = "로그인 메서드", description = "로그인 메서드입니다.")
    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginMemberRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

    @Operation(summary = "로그아웃 메서드", description = "로그아웃 메서드입니다.")
    @PostMapping("/logout")
    public ResponseEntity<Message> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return memberService.logout(userDetails.getMember(), request);
    }


//    @Operation(summary = "이메일인증 및 회원가입 메서드", description = "이메일인증 및 회원가입 메서드입니다.")
//    @PostMapping("/signup")
//    public ResponseEntity<Message> signupEmail(@Valid @RequestBody SignupMemberRequestDto requestDto) throws Exception {
//
//        return emailVerifiedService.signup(requestDto);
//    }
//
//    @Operation(summary = "이메일인증 메서드", description = "이메일인증 메서드입니다.")
//    @PostMapping("/verify-email")
//    public ResponseEntity<Message> verifyEmail(@RequestBody VerifyEmailRequestDto requestDto) {
//        return emailVerifiedService.verifyEmail(requestDto.getEmail(), requestDto.getEmailCode());
//    }

}
