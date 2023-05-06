package com.example.weluvwine.domain.recommend.repository;

import com.example.weluvwine.domain.recommend.entity.Recommend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommend, Long> {
    Optional<Recommend> findByWineIdAndMemberId(Long wineId, Long memberId);
}
