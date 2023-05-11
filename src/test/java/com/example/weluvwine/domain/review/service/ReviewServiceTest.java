package com.example.weluvwine.domain.review.service;

import com.example.weluvwine.domain.member.dto.LoginMemberRequestDto;
import com.example.weluvwine.domain.member.dto.SignupMemberRequestDto;
import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.service.MemberService;
import com.example.weluvwine.domain.recommend.repository.RecommendRepository;
import com.example.weluvwine.domain.recommend.service.RecommendService;
import com.example.weluvwine.domain.review.dto.ReviewRequestDto;
import com.example.weluvwine.domain.review.entity.Review;
import com.example.weluvwine.domain.review.repository.ReviewRepository;
import com.example.weluvwine.domain.wine.entity.Wine;
import com.example.weluvwine.domain.wine.entity.WineType;
import com.example.weluvwine.domain.wine.repository.WineRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.util.Message;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Autowired
    WineRepository wineRepository;

    @Autowired
    RecommendRepository recommendRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    RecommendService recommendService;

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Mock
    HttpServletResponse response;

    @BeforeEach
    void signup() {
        SignupMemberRequestDto memberRequestDto = new SignupMemberRequestDto("test123", "test123123!", "김르탄임");
        memberService.signup(memberRequestDto);
    }

    @Nested
    @DisplayName("CRUD 성공 CASE")
    class crudSuccessTest {

        @Test
        @DisplayName("리뷰 작성 성공")
        void createReview() {
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);


            ReviewRequestDto review = new ReviewRequestDto("리뷰");
            ResponseEntity<Message> responseEntity = reviewService.createReview(wine.getId(), review, member);

            String message = responseEntity.getBody().getMessage();
            Review review2 = (Review) responseEntity.getBody().getData();
            assertEquals(message, "리뷰 작성 성공");
            assertEquals(review2.getContent().toString(), "리뷰");

        }

        @Test
        @DisplayName("리뷰 수정 성공")
        void updateReview() {
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
            Review review = new Review(1L, "작성", member, wine, false);

            ReviewRequestDto reviewUpdate = new ReviewRequestDto("수정");
            ResponseEntity<Message> response = reviewService.updateReview(review.getId(), reviewUpdate, member);

            String message = response.getBody().getMessage();
            Review review1 = (Review) response.getBody().getData();
            assertEquals(message, "리뷰 수정 성공");

        }

        @Test
        @DisplayName("리뷰 삭제 성공")
        void deleteReview() {
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
            Review review = new Review(10L, "작성", member, wine, false);


            ResponseEntity<Message> response = reviewService.deleteReview(review.getId(), member);

            String message = response.getBody().getMessage();
            assertEquals(message, "리뷰 삭제 성공");
        }

        @Test
        @DisplayName("리뷰 조회 성공")
        void getReviewList() {
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            LoginMemberRequestDto loginMemberRequestDto2 = new LoginMemberRequestDto("test987", "test123123!");
            ResponseEntity<Message> loginResponse2 = memberService.login(loginMemberRequestDto2, response);
            Member member2 = (Member) loginResponse2.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            Review review = new Review(1L, "작성", member, wine, false);
            Review review2 = new Review(2L, "수정", member2, wine, false);
            Review review3 = new Review(3L, "너무 어려워요", member, wine, false);
            reviewRepository.save(review);
            reviewRepository.save(review2);
            reviewRepository.save(review3);

            ResponseEntity<Message> response = reviewService.getReviewList(member);

            List<Review> reviewList = (List<Review>) response.getBody().getData();
            assertEquals(reviewList.get(0).getContent(), "작성");
            assertEquals(reviewList.get(1).getContent(), "너무 어려워요");
        }

    }

    @Nested
    @DisplayName("CRUD 실패 CASE")
    class crudFailTest {
        @Test
        @DisplayName("리뷰 작성 실패 - 리뷰의 내용이 없음")
        void nonContentForReview() {
            // Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            ReviewRequestDto requestDto = new ReviewRequestDto("");

            // When & Then
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.createReview(wine.getId(), requestDto, member);
            });

            assertEquals("글의 내용이 없습니다", exception.getMessage());
        }

        @Test
        @DisplayName("리뷰 작성 실패 - memberId 유효하지 않음")
        void invalidMemberIdForReview() {
            // Given
            Member member = new Member("stranger12", "pw123412!!", "이상한사람");
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            ReviewRequestDto requestDto = new ReviewRequestDto("리뷰 달아봅시다.");


            // When & Then
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.createReview(wine.getId(), requestDto, member);
            });

            assertEquals("AccessToken Expired.", exception.getMessage());

        }


        @Test
        @DisplayName("리뷰 수정 실패 - 해당 게시글 찾을 수 없음")
        void notFoundReviewForUpdate() {
            //Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            ReviewRequestDto requestDto = new ReviewRequestDto("나는 언제 잘 수 있을까?");

            // When & Then
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.updateReview(0L, requestDto, member);
            });

            assertEquals("해당 게시글을 찾을 수 없습니다.", exception.getMessage());


        }

        @Test
        @DisplayName("리뷰 수정 실패 - 작성자가 아님")
        void notMatchMemberIdForUpdate() {
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member1 = (Member) loginResponse.getBody().getData();
            member1.setId(1L);
            Member member2 = new Member("얘는", "다른친구", "이지롱");
            member2.setId(2L);
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
            Review review = new Review(1L, "작성", member1, wine, false);

            ReviewRequestDto reviewUpdate = new ReviewRequestDto("수정");

            CustomException e = assertThrows(CustomException.class, () -> reviewService.updateReview(review.getId(), reviewUpdate, member2));
            assertEquals("작성자만 수정,삭제할 수 있습니다.", e.getErrorCode().getDetail());
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 해당 게시글 찾을 수 없음")
        void notFoundReviewForDelete() {
            //Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);

            // When & Then
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.deleteReview(0L, member);
            });

            assertEquals("해당 게시글을 찾을 수 없습니다.", exception.getMessage());
        }

        @Test
        @DisplayName("리뷰 삭제 실패 - 작성자가 아님")
        void notMatchMemberIdForDelete() {
            //Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();
            member.setId(1L);
            Wine wine = new Wine(1L, "이미지주소", "샤또 킹스 블랑", "프랑스", "까베르네 소비뇽", "2019", "와이너리", WineType.RED, "와인입니다아", 0);
            Review review = new Review(1L, "리뷰 작성", member, wine, false);


            // When
            Member stranger = new Member("stranger12", "pw123412!!", "이상한사람");
            stranger.setId(2L);
            // Then
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.deleteReview(review.getId(), stranger);
            });

            assertEquals("작성자만 수정,삭제할 수 있습니다.", exception.getErrorCode().getDetail());
        }


        @Test
        @DisplayName("마이페이지 리뷰 조회 실패 - 유효하지 않은 memberId")
        void cannotGetReviews() {
            // Given
            LoginMemberRequestDto loginMemberRequestDto = new LoginMemberRequestDto("test123", "test123123!");
            ResponseEntity<Message> loginResponse = memberService.login(loginMemberRequestDto, response);
            Member member = (Member) loginResponse.getBody().getData();

            // When & Then
            Member stranger = new Member("stranger12", "pw123412!!", "이상한사람");
            CustomException exception = assertThrows(CustomException.class, () -> {
                reviewService.getReviewList(stranger);
            });

            assertEquals("로그인 후 이용해주세요.", exception.getMessage());
        }
    }
}