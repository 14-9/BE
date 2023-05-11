package com.example.weluvwine.domain.member.service;


import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.repository.MemberRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.security.jwt.JwtUtil;
import com.example.weluvwine.security.jwt.TokenDto;
import com.example.weluvwine.security.refreshToken.RefreshTokenRepository;
import com.example.weluvwine.util.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Mock
    HttpServletResponse response;

    @Mock
    HttpServletRequest request;

    @BeforeEach
    void signup(){
        SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
        memberService.signup(memberRequestDto);
    }

    @Nested
    @DisplayName("회원가입 TEST")
    class signup {
        @Test
        @DisplayName("회원가입 성공 CASE")
        void signupTest() {
            // Given
            SignupMemberRequestDto member = new SignupMemberRequestDto("test12", "test41234!!", "김르탄");

            // When
            ResponseEntity<Message> signupMember = memberService.signup(member);

            // Then
            Member member1 = (Member) signupMember.getBody().getData();
            Assertions.assertThat(member1.getMemberId()).isEqualTo(member.getMemberId());
        }

        @Nested
        @DisplayName("회원가입 실패 CASE")
        class signupFail{
            @Test
            @DisplayName("회원가입시, memberId 가 중복되었을 경우 예외를 발생시킨다.")
            void duplicateMemberId() {
                // Given
                SignupMemberRequestDto memberRequestDto2 = new SignupMemberRequestDto("test123", "test41234!!", "김르탄");

                // When
                CustomException e = assertThrows(CustomException.class, () -> memberService.signup(memberRequestDto2));

                // Then
                assertEquals("중복된 아이디 입니다.", e.getErrorCode().getDetail());
            }

            @Test
            @DisplayName("회원가입시, nickname 이 중복되었을 경우 예외를 발생시킨다.")
            void duplicateNickname() {
                // Given
                SignupMemberRequestDto memberRequestDto2 = new SignupMemberRequestDto("test12", "test41234!!", "김르탄임");

                // When
                CustomException e = assertThrows(CustomException.class, () -> memberService.signup(memberRequestDto2));

                // Then
                assertEquals("중복된 닉네임 입니다.", e.getErrorCode().getDetail());
            }
        }

    }

    @Nested
    @DisplayName("로그인 TEST")
    class login{

        @Test
        @DisplayName("로그인 성공 CASE")
        void loginTest(){
            // Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123","test123123!");
            //When
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            //Then
            String message = loginResponse.getBody().getMessage();
            assertEquals("로그인 성공", message);
        }

        @Nested
        @DisplayName("로그인 실패 CASE")
        class loginFail{

            @Test
            @DisplayName("없는 회원 = 틀린 아이디")
            void loginIdInconsistencyTest () {
                // Given
                LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test   123","test123123!");
                // When
                CustomException e = assertThrows(CustomException.class, () -> memberService.login(loginMemberRequestDto, response));
                // Then
                assertEquals("회원을 찾을 수 없습니다.", e.getErrorCode().getDetail());
            }

            @Test
            @DisplayName("틀린 비밀번호")
            void loginPwInconsistencyTest () {
                // Given
                LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123","틀렸지롱!");
                // When
                CustomException e = assertThrows(CustomException.class, () -> memberService.login(loginMemberRequestDto, response));
                // Then
                assertEquals("잘못된 비밀번호 입니다.", e.getErrorCode().getDetail());
            }

        }
    }


    @Nested
    @DisplayName("로그아웃 TEST")
    class logout {

        @Test
        @DisplayName("로그아웃 성공")
        void logoutSuccessTest() {
            // Given
            Member member = new Member("test123", "test123123!", "김르탄임");
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(member.getMemberId(),member.getPassword());
            memberService.login(loginMemberRequestDto, response);
            TokenDto tokenDto = jwtUtil.createAllToken(member.getMemberId());
            String accessToken = tokenDto.getAccessToken();
            Mockito.when(request.getHeader("ACCESS_KEY")).thenReturn(accessToken);

            // When
            ResponseEntity<Message> logoutResponse = memberService.logout(member, request);

            // Then
            String message = logoutResponse.getBody().getMessage();
            assertEquals("로그아웃 성공", message);
        }

        @Test
        @DisplayName("이미 로그아웃 진행")
        void logoutFailTest() {
            // Given
            Member member = new Member("test123", "test123123!", "김르탄임");
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto(member.getMemberId(),member.getPassword());
            memberService.login(loginMemberRequestDto, response);
            TokenDto tokenDto = jwtUtil.createAllToken(member.getMemberId());
            String accessToken = tokenDto.getAccessToken();
            Mockito.when(request.getHeader("ACCESS_KEY")).thenReturn(accessToken);

            // When
            ResponseEntity<Message> logoutResponse = memberService.logout(member, request);

            // Then
            CustomException e = assertThrows(CustomException.class, () -> memberService.logout(member, request));
            assertEquals("회원을 찾을 수 없습니다.", e.getErrorCode().getDetail());
        }

    }
}