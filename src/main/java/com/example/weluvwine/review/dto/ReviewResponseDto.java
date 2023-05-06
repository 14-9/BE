package com.example.weluvwine.review.dto;

import com.example.weluvwine.review.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public ReviewResponseDto(Review review){
        this.id = review.getId();
        this.content = review.getContent();
        this.createdAt = review.getCreatedAt();
        this.modifiedAt = review.getModifiedAt();
    }
}
