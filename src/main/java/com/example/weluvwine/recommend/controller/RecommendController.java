package com.example.weluvwine.recommend.controller;

import com.example.weluvwine.recommend.service.RecommendService;
import com.example.weluvwine.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecommendController {

    private final RecommendService recommendService;

    @PostMapping("/api/recommend/{wine-id}")
    public ResponseEntity recommendWine(@PathVariable(name = "wine-id") Long wineId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return recommendService.recommendWine(wineId, userDetails.getMember());
    }
}
