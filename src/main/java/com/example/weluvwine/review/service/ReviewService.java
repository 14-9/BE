package com.example.weluvwine.review.service;

import com.example.weluvwine.member.entity.Member;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.review.dto.ReviewRequestDto;
import com.example.weluvwine.review.entity.Review;
import com.example.weluvwine.review.repository.ReviewRepository;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    //리뷰 작성
    public ResponseEntity<Message> createPost(ReviewRequestDto requestDto, Member member) {
        Review review = new Review(requestDto, member);
        reviewRepository.save(review);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 작성 성공", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    //리뷰 수정
    public ResponseEntity<Message> updatePost(Long reviewId, ReviewRequestDto requestDto, Member member){
        Review review = findReviewById(reviewId);
        isUserReview(review, member);
        review.update(requestDto);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 수정 성공", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    //리뷰 삭제
    public ResponseEntity<Message> deletePost(Long reviewId, Member member){
        Review review = findReviewById(reviewId);
        isUserReview(review, member);
        reviewRepository.deleteById(reviewId);
        Message message = Message.setSuccess(StatusEnum.OK,"리뷰 삭제 성공", null);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    //리뷰 유무 확인
    public Review findReviewById(Long id){
        return reviewRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }
    //작성자 리뷰 확인
    public void isUserReview(Review review, Member member){
        if(!review.getId().equals(member.getId())){
            throw new IllegalArgumentException("리뷰 작성자만 수정, 삭제 가능합니다.");
        }
    }
}
