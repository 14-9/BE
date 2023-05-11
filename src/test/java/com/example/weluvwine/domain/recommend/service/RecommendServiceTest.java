package com.example.weluvwine.domain.recommend.service;

import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.repository.MemberRepository;
import com.example.weluvwine.domain.member.service.MemberService;
import com.example.weluvwine.domain.recommend.repository.RecommendRepository;
import com.example.weluvwine.domain.wine.entity.Wine;
import com.example.weluvwine.domain.wine.entity.WineType;
import com.example.weluvwine.domain.wine.repository.WineRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.util.Message;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class RecommendServiceTest {

    @Autowired
    WineRepository wineRepository;

    @Autowired
    RecommendRepository recommendRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    RecommendService recommendService;

    @Mock
    HttpServletResponse response;

    @Nested
    @DisplayName("좋아요 성공 CASE")
    class recommendSuccessTest {

        @BeforeEach
        void signup() {
            SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
            memberService.signup(memberRequestDto);
        }

        @Order(1)
        @Test
        @DisplayName("좋아요 성공!")
        void recommendTest() {
            // Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            // When
            ResponseEntity<Message> recommend = recommendService.recommendWine(wine.getId(), member);
            // Then
            long count = (long) recommend.getBody().getData();
            assertEquals(count, 1L);

        }

        @Order(2)
        @Test
        @DisplayName("좋아요를 누르고 한 번 더 눌러서 취소한 경우 - 성공")
        void recommendTwiceForDislike() {
            // Given
            SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
            ResponseEntity<Message> signupDto = memberService.signup(memberRequestDto);
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            // When
            ResponseEntity<Message> recommend = recommendService.recommendWine(wine.getId(), member);
            ResponseEntity<Message> recommend2 = recommendService.recommendWine(wine.getId(), member);
            // Then
            long count = (long) recommend2.getBody().getData();
            assertEquals(count, 0);
        }


    }

//    @Nested
//    @DisplayName("좋아요 실패 CASE")
//    class recommendFailTest {
//        //        @Test
////        @DisplayName("접근 권한 없음 - memberId 유효하지 않음")
////        void noHaveAuthorization() {
////            // Given
//////            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
//////            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
////
////            Member member = new Member(2L, "틀린녀석", "pwasdf213!", "유효하지 않은놈");
////            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
////
////            // When
//////            ResponseEntity<Message> recommendResponse = recommendService.recommendWine(wine.getId(), member);
////
////            // Then
////            CustomException e = assertThrows(CustomException.class, () -> recommendService.recommendWine(wine.getId(), member));
////            assertEquals("AccessToken Expired.", e.getErrorCode().getDetail());
////        }
//        // 깨달은걸로 좀 깨닫게 해주세요......아 깨달은거 아닌듯합ㄴ디ㅏ 유감.
//        @Test
//        @DisplayName("접근 권한 없음 - memberId 유효하지 않음")
//        void noHaveAuthorization() {
//            // Given
////            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
////            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
//            // 로그인 후 정상적인 토큰을 받아옴
//
//            Member member = new Member(2L, "틀린녀석", "pwasdf213!", "유효하지 않은놈");
//            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
//
//            // When
//
//
//            // Then
//            assertEquals("AccessToken Expired.", e.getErrorCode().getDetail());
//        }
//
//
//        @Test
//        @DisplayName("와인 id 유효하지 않음")
//        void noHaveWineId() {
//        }
//
//        @Test
//        @DisplayName("이거 왜 (빠르게)2번 눌렀데는 마이너스 안돼? 경우")
//            // 이거 인간적으로 너무 어려운거 아님?
//        void recommendTwiceButNoMinus() {
//
//        }
//
//        @Test
//        @DisplayName("왜 안올라감? 경우")
//        void whyNotRecommendUp() {
//
//        }
//
//
//    }


}


//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Transactional
//class RecommendServiceTest {
//
//    @Autowired
//    WineRepository wineRepository;
//
//    @Autowired
//    MemberService memberService;
//
//    @Autowired
//    RecommendService recommendService;
//
//    @Nested
//    @DisplayName("좋아요 성공 CASE")
//    class recommendSuccessTest {
//
//        @Test
//        @DisplayName("좋아요 성공!")
//        void recommendTest() {
//            // Given
//            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
//            SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
//            ResponseEntity<Message> signupResponse = memberService.signup(memberRequestDto);
//            Long memberId = signupResponse.getBody().getId();
//            // When
//            ResponseEntity<Message> recommendResponse = recommendService.recommendWine(wine.getId(), memberId);
//            // Then
//            assertEquals("좋아요 성공", recommendResponse.getBody().getMessage());
//        }
//
//        @Test
//        @DisplayName("좋아요를 누르고 취소한 경우 - 성공")
//        void recommendTwiceForDislike() {
//            // Given
//            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
//            SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
//            ResponseEntity<Message> signupResponse = memberService.signup(memberRequestDto);
//            Long memberId = signupResponse.getBody().getId();
//            recommendService.recommendWine(wine.getId(), memberId);
//            // When
//            ResponseEntity<Message> cancelRecommendResponse = recommendService.cancelRecommendWine(wine.getId(), memberId);
//            // Then
//            assertEquals("좋아요 취소 성공", cancelRecommendResponse.getBody().getMessage());
//        }
//    }
//
//    @Nested
//    @DisplayName("좋아요 실패 CASE")
//    class recommendFail
//@Nested
//@DisplayName("좋아요 실패 CASE")
//class recommendFailTest {
//    @Test
//    @DisplayName("접근 권한 없음 - memberId 유효하지 않음")
//    void noHaveAuthorization() {
//// Given
//        Long invalidMemberId = 1000L;
//        Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
// When
//ResponseEntity<Message> recommendResponse = recommendService.recommendWine(wine.getId(), invalidMemberId);
//
//    // Then
//    assertEquals("접근 권한이 없습니다.", recommendResponse.getBody().getMessage());
//        }
//
//@Test
//@DisplayName("와인 id 유효하지 않음")
//void noHaveWineId() {
//        // Given
//        Long invalidWineId = 1000L;
//        SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
//        ResponseEntity<Message> signupResponse = memberService.signup(memberRequestDto);
//        Long memberId = signupResponse.getBody().getId();
//
//        // When
//        ResponseEntity<Message> recommendResponse = recommendService.recommendWine(invalidWineId, memberId);
//
//        // Then
//        assertEquals("와인을 찾을 수 없습니다.", recommendResponse.getBody().getMessage());
//        }
//
//@Test
//@DisplayName("이미 좋아요를 누른 경우 - 실패")
//void alreadyRecommended() {
//        // Given
//        Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
//        SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
//        ResponseEntity<Message> signupResponse = memberService.signup(memberRequestDto);
//        Long memberId = signupResponse.getBody().getId();
//        recommendService.recommendWine(wine.getId(), memberId);
//
//        // When
//        ResponseEntity<Message> recommendResponse = recommendService.recommendWine(wine.getId(), memberId);
//
//        // Then
//        assertEquals("이미 좋아요한 와인입니다.", recommendResponse.getBody().getMessage());
//        }
//}