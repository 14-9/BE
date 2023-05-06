package com.example.weluvwine.review.controller;

import com.example.weluvwine.review.dto.ReviewResponseDto;
import com.example.weluvwine.security.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.review.dto.ReviewRequestDto;
import com.example.weluvwine.review.service.ReviewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Tag(name = "ReviewController", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰 작성
    @PostMapping("/{wine-id}")
    public ResponseEntity<Message> createPost(@PathVariable(name = "wine-id")Long wineId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.createPost(wineId, requestDto, userDetails.getMember());
    }
    //리뷰 수정
    @PutMapping("/{review-id}")
    public ResponseEntity<Message> updatePost(@PathVariable(name = "review-id")Long reviewId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.updatePost(reviewId, requestDto, userDetails.getMember());
    }
    //리뷰 삭제
    @DeleteMapping("/{review-id}")
    public ResponseEntity<Message> deletePost(@PathVariable(name = "review-id")Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.deletePost(reviewId, userDetails.getMember());
    }
    //마이페이지 조회
    @GetMapping("/{member-id}")
    public ResponseEntity<Message> getReviewList(@PathVariable(name = "member-id")Long memberId){
        return reviewService.getReviewList(memberId);
    }
}

