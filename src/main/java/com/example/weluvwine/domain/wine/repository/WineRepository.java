package com.example.weluvwine.domain.wine.repository;

import com.example.weluvwine.domain.wine.entity.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineRepository extends JpaRepository<Wine, Long> {
    List<Wine> findByNameContaining(String searchKeyword);

    List<Wine> findTop8ByOrderByRecommendCountDesc();
}
