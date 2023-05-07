package com.example.weluvwine.domain.review.repository;

import com.example.weluvwine.domain.review.dto.ReviewResponseDto;
import com.example.weluvwine.domain.review.entity.Review;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllById(Long memberId);
    List<Review> findAllByWineId(Long wineId);
}
