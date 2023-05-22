package com.example.weluvwine.domain.member.service;


import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.repository.MemberRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.redis.util.RedisUtil;
import com.example.weluvwine.security.jwt.JwtUtil;
import com.example.weluvwine.security.jwt.TokenDto;
import com.example.weluvwine.security.refreshToken.RefreshToken;
import com.example.weluvwine.security.refreshToken.RefreshTokenRepository;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.weluvwine.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    // 회원가입
    @Transactional
    public ResponseEntity<Message> signup(SignupMemberRequestDto requestDto) {
        String memberId = requestDto.getMemberId();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

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

        Member member = new Member(memberId, password, nickname);
        memberRepository.save(member);
        Message message = Message.setSuccess(StatusEnum.OK, "회원가입 성공", member);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }


    // 로그인
    @Transactional
    public ResponseEntity<Message> login(LoginMemberRequestDto requestDto, HttpServletResponse response) {
        String memberId = requestDto.getMemberId();
        String password = requestDto.getPassword();

        // 회원 가입한 회원인지 check
        Member foundMember = memberRepository.findByMemberId(memberId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        // 비밀번호 check
        if (!passwordEncoder.matches(password, foundMember.getPassword())) {
            throw new CustomException(INVALID_PASSWORD);
        }
        //아이디 정보로 토큰 생성
        TokenDto tokenDto = jwtUtil.createAllToken(memberId);

        //Refresh 토큰 있는지 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(foundMember.getMemberId());

        if (refreshToken.isPresent()) {
            refreshTokenRepository.save(refreshToken.get().updateToken(tokenDto.getRefreshToken()));
        } else {
            RefreshToken newToken = new RefreshToken(tokenDto.getRefreshToken(), foundMember.getMemberId());
            refreshTokenRepository.save(newToken);
        }
        //response 헤더에 AccessToken / RefreshToken
        setHeader(response, tokenDto);
        Message message = Message.setSuccess(StatusEnum.OK, "로그인 성공", foundMember);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //로그아웃
    @Transactional
    public ResponseEntity<Message> logout(Member member, HttpServletRequest request) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByMemberId(member.getMemberId());

        String accessToken = request.getHeader("ACCESS_KEY").substring(7);
        if(refreshToken.isPresent()){
            Long tokenTime = jwtUtil.getExpirationTime(accessToken);
            redisUtil.setBlackList(accessToken, "access_token", tokenTime);
            refreshTokenRepository.deleteByMemberId(member.getMemberId());
            Message message = Message.setSuccess(StatusEnum.OK,"로그아웃 성공", member.getMemberId());
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        throw new CustomException(USER_NOT_FOUND);
    }

    // 헤더 셋팅
    private void setHeader(HttpServletResponse response, TokenDto tokenDto) {
        response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
        response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
    }

}