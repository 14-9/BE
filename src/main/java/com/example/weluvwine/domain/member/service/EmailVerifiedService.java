package com.example.weluvwine.domain.member.service;

import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.entity.EmailCode;
import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.repository.EmailCodeRepository;
import com.example.weluvwine.domain.member.repository.MemberRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.redis.util.RedisUtil;
import com.example.weluvwine.security.jwt.JwtUtil;
import com.example.weluvwine.security.refreshToken.RefreshTokenRepository;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.weluvwine.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class EmailVerifiedService {


    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final EmailCodeRepository emailCodeRepository;

    @Transactional
    public ResponseEntity<Message> signup(SignupMemberRequestDto requestDto) throws Exception {
        String memberId = requestDto.getMemberId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();
        String email = requestDto.getEmail();

        // 회원 중복 check
        Optional<Member> foundMember = memberRepository.findByMemberId(memberId);
        if (foundMember.isPresent()) {
            throw new CustomException(DUPLICATE_IDENTIFIER);
        }
        //중복 닉네임 체크
        Optional<Member> foundMemberNickname = memberRepository.findByNickname(nickname);
        if (foundMemberNickname.isPresent()) {
            throw new CustomException(DUPLICATE_NICKNAME);
        }
        String emailCode = emailService.sendSimpleMessage(requestDto.getEmail());
        EmailCode Code = new EmailCode(requestDto.getEmail(), emailCode);

        Member member = new Member(memberId, password, nickname);
        member.setEmail(email);
        member.setEmailCode(Code);
        memberRepository.save(member);
        Message message = Message.setSuccess(StatusEnum.OK, "회원가입 성공", member);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    public ResponseEntity<Message> verifyEmail(String email, String emailCode) {

        Optional<EmailCode> foundEmailCode = emailCodeRepository.findByEmail(email);

        if (!foundEmailCode.isPresent()) {
            throw new CustomException(EMAIL_NOT_FOUND);
        }

        EmailCode code = foundEmailCode.get();

        // 저장된 이메일 인증 코드와 입력된 이메일 인증 코드를 비교
        if (!code.getEmailCode().equals(emailCode)) {
            throw new CustomException(INVALID_EMAIL_CODE);
        }
        Optional<Member> memberEmailCode = memberRepository.findByEmail(email);
        if (!memberEmailCode.isPresent()) {
            throw new CustomException(USER_NOT_FOUND);
        }
        Member member = memberEmailCode.get();
        member.setEmailVerified(true);
        memberRepository.save(member);

        Message message = Message.setSuccess(StatusEnum.OK, "이메일 인증 성공", member);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

}
