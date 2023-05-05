package com.example.weluvwine.member.controller;

import com.example.weluvwine.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.member.service.MemberService;
import com.example.weluvwine.util.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
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

    // 로그아웃
//    @GetMapping("/logout")
//    public ResponseEntity<Message> logout(@AuthenticationPrincipal MemberDetailsImpl memberDetails, HttpServletRequest request) {
//        return memberService.logout(memberDetails.getMember(), request);
//    }

}
