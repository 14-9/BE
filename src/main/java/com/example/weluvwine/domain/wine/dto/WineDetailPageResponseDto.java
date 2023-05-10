package com.example.weluvwine.domain.wine.dto;

import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.review.dto.ReviewListResponseDto;
import com.example.weluvwine.domain.wine.entity.Wine;
import lombok.Getter;

import java.util.List;

@Getter
public class WineDetailPageResponseDto {

    private Wine wine;
    private boolean recommend;
    private Member member;
    private List<ReviewListResponseDto> review;

    public WineDetailPageResponseDto(Wine wine, boolean recommend,Member member, List<ReviewListResponseDto> review){
        this.wine = wine;
        this.recommend = recommend;
        this.member = member;
        this.review = review;
    }
}
