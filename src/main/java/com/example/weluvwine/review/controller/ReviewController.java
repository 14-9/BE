package com.example.weluvwine.review.controller;

import com.example.weluvwine.security.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.review.dto.ReviewRequestDto;
import com.example.weluvwine.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    //리뷰 작성
    @PostMapping("/review")
    public ResponseEntity<Message> createPost(@RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.createPost(requestDto, userDetails.getMember());
    }
    //리뷰 수정
    @PutMapping("/review/{review-id}/update")
    public ResponseEntity<Message> updatePost(@PathVariable(name = "review-id")Long reviewId, @RequestBody ReviewRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.updatePost(reviewId, requestDto, userDetails.getMember());
    }
    //리뷰 삭제
    @DeleteMapping("/review/{review-id}/delete")
    public ResponseEntity<Message> deletePost(@PathVariable(name = "review-id")Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return reviewService.deletePost(reviewId, userDetails.getMember());
    }
}

