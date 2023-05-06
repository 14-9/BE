package com.example.weluvwine.domain.recommend.controller;

import com.example.weluvwine.domain.recommend.service.RecommendService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "RecommendController", description = "추천하기 관련 API")
public class RecommendController {

    private final RecommendService recommendService;

    @Operation(summary = "추천하기 메서드", description = "추천하기 메서드입니다.")
    @PostMapping("/api/recommend/{wine-id}")
    public ResponseEntity recommendWine(@PathVariable(name = "wine-id") Long wineId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recommendService.recommendWine(wineId, userDetails.getMember());
    }
}
