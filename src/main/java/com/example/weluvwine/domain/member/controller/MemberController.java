package com.example.weluvwine.domain.member.controller;

import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.service.MemberService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import com.example.weluvwine.util.Message;
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
@RequestMapping("/api/user")
@Tag(name = "MemberController", description = "유저 관련 API")
public class MemberController {
    private final MemberService memberService;


    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Message> signup(@Valid @RequestBody SignupMemberRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Message> login(@RequestBody LoginMemberRequestDto requestDto, HttpServletResponse response) {
        return memberService.login(requestDto, response);
    }

     //로그아웃
    @GetMapping("/logout")
    public ResponseEntity<Message> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest request) {
        return memberService.logout(userDetails.getMember(), request);
    }

}
