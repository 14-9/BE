package com.example.weluvwine.review.repository;

import com.example.weluvwine.review.dto.ReviewResponseDto;
import com.example.weluvwine.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllById(Long memberId);
}
