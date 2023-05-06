package com.example.weluvwine.domain.review.controller;

import com.example.weluvwine.domain.review.dto.ReviewRequestDto;
import com.example.weluvwine.domain.review.service.ReviewService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "ReviewController", description = "리뷰 관련 API")
public class ReviewController {

    private final ReviewService reviewService;
    @Operation(summary = "리뷰작성 메서드", description = "리뷰작성 메서드입니다.")
    @PostMapping("/review/{wine-id}")
    public ResponseEntity<Message> createPost(@PathVariable(name = "wine-id")Long wineId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.createPost(wineId, requestDto, userDetails.getMember());
    }
    @Operation(summary = "리뷰수정 메서드", description = "리뷰수정 메서드입니다.")
    @PutMapping("/review/{review-id}")
    public ResponseEntity<Message> updatePost(@PathVariable(name = "review-id")Long reviewId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.updatePost(reviewId, requestDto, userDetails.getMember());
    }
    @Operation(summary = "리뷰삭제 메서드", description = "리뷰삭제 메서드입니다.")
    @DeleteMapping("/review/{review-id}")
    public ResponseEntity<Message> deletePost(@PathVariable(name = "review-id")Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.deletePost(reviewId, userDetails.getMember());
    }
    @Operation(summary = "마이페이지 조회 메서드", description = "마이페이지 조회 메서드입니다.")
    @GetMapping("/mypage/{member-id}")
    public ResponseEntity<Message> getReviewList(@PathVariable(name = "member-id")Long memberId){
        return reviewService.getReviewList(memberId);
    }
}

