package com.example.weluvwine.review.repository;

import com.example.weluvwine.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
