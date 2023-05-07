package com.example.weluvwine.domain.review.service;

import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.review.dto.ReviewRequestDto;
import com.example.weluvwine.domain.review.dto.ReviewResponseDto;
import com.example.weluvwine.domain.review.entity.Review;
import com.example.weluvwine.domain.review.repository.ReviewRepository;
import com.example.weluvwine.domain.wine.entity.Wine;
import com.example.weluvwine.domain.wine.repository.WineRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static com.example.weluvwine.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WineRepository wineRepository;

    //리뷰 작성
    public ResponseEntity<Message> createPost(Long wineId, ReviewRequestDto requestDto, Member member) {
        Wine wine = findWineById(wineId);
        Review review = new Review(requestDto, member, wine);
        reviewRepository.save(review);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 작성 성공");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //리뷰 수정
    public ResponseEntity<Message> updatePost(Long reviewId, ReviewRequestDto requestDto, Member member){
        Review review = findReviewById(reviewId);
        isUserReview(review, member);
        review.update(requestDto);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 수정 성공");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    //리뷰 삭제
    public ResponseEntity<Message> deletePost(Long reviewId, Member member){
        Review review = findReviewById(reviewId);
        isUserReview(review, member);
        reviewRepository.deleteById(reviewId);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 삭제 성공");
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //마이페이지 리뷰 조회
    public ResponseEntity<Message> getReviewList(Long memberId) {
        List<ReviewResponseDto> reviewList = reviewRepository.findAllById(memberId).stream()
                .map(ReviewResponseDto::new).collect(Collectors.toList());
        Message message = Message.setSuccess(StatusEnum.OK,"요청 성공", reviewList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //와인 존재 확인
    private Wine findWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new CustomException(WINE_NOT_FOUND));
    }
    //리뷰 유무 확인
    public Review findReviewById(Long id){
        return reviewRepository.findById(id).orElseThrow(
                () -> new CustomException(POST_NOT_FOUND));
    }
    //작성자 리뷰 확인
    public void isUserReview(Review review, Member member){
        if(!review.getMember().getId().equals(member.getId())){
            throw new CustomException(NOT_AUTHORIZED_USER);
        }
    }
}
