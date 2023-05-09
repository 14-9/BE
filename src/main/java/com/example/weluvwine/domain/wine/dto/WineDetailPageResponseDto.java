package com.example.weluvwine.domain.wine.dto;

import com.example.weluvwine.domain.review.dto.ReviewListResponseDto;
import com.example.weluvwine.domain.wine.entity.Wine;
import lombok.Getter;

import java.util.List;

@Getter
public class WineDetailPageResponseDto {

    private Wine wine;
    private boolean recommend;
    private List<ReviewListResponseDto> review;

    public WineDetailPageResponseDto(Wine wine, boolean recommend, List<ReviewListResponseDto> review){
        this.wine = wine;
        this.recommend = recommend;
        this.review = review;
    }
}
