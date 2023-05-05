package com.example.weluvwine.wine.controller;

import com.example.weluvwine.security.UserDetailsImpl;
import com.example.weluvwine.wine.service.WineService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WineController {
    private final WineService wineService;

    @GetMapping("/api/search")
    public ResponseEntity searchWine(@RequestParam String searchKeyword, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.searchWine(searchKeyword);
    }
    @GetMapping("/api/review/{reviewId}/read")
    public ResponseEntity readWine(@PathVariable Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.readWine(reviewId);
    }
}
