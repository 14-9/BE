package com.example.weluvwine.member.service;

import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.jwt.JwtUtil;
import com.example.weluvwine.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.member.entity.Member;
import com.example.weluvwine.member.repository.MemberRepository;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.weluvwine.exception.ErrorCode.DUPLICATE_IDENTIFIER;
import static com.example.weluvwine.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    @Transactional
    public ResponseEntity<Message> signup(SignupMemberRequestDto requestDto) {
        String memberId = requestDto.getMemberId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        // 회원 중복 check
        Optional<Member> foundMember = memberRepository.findMemberByMemberId(memberId);
        if (foundMember.isPresent()) {
            throw new CustomException(DUPLICATE_IDENTIFIER);
        }

        Member member = new Member(requestDto);
        memberRepository.save(member);
        Message message = Message.setSuccess(StatusEnum.OK, "회원가입 성공", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    // 로그인
    @Transactional
    public ResponseEntity<Message> login(LoginMemberRequestDto requestDto, HttpServletResponse response) {
        String memberId = requestDto.getMemberId();
        String password = requestDto.getPassword();

        // 회원 가입한 회원인지 check
        Member foundMember = memberRepository.findMemberByMemberId(memberId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        // 비밀번호 check
        if (!passwordEncoder.matches(password, foundMember.getPassword())) {
            throw new CustomException(USER_NOT_FOUND);
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(foundMember.getMemberId()));

        Message message = Message.setSuccess(StatusEnum.OK, "로그인 성공", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

      //로그아웃
//    @Transactional
//    public ResponseEntity<Message> logout(LoginMemberRequestDto requestDto, HttpServletResponse response) {
//
//    }


}
