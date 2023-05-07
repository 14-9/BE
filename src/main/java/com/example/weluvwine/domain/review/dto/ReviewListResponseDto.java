package com.example.weluvwine.domain.review.dto;

import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewListResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Member member;

    public ReviewListResponseDto(Review review){
        this.id = review.getId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
        this.member = review.getMember();
    }
}
