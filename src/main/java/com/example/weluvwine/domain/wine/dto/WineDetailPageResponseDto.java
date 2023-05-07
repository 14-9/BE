package com.example.weluvwine.domain.wine.dto;

import com.example.weluvwine.domain.review.dto.ReviewListResponseDto;
import com.example.weluvwine.domain.wine.entity.Wine;
import lombok.Getter;

import java.util.List;

@Getter
public class WineDetailPageResponseDto {

    private Object wine;
    private Object review;

    public WineDetailPageResponseDto(Wine wine, List<ReviewListResponseDto> review){
        this.wine = wine;
        this.review = review;
    }
}
