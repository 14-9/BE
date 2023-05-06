package com.example.weluvwine.domain.review.repository;

import com.example.weluvwine.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllById(Long memberId);
}
