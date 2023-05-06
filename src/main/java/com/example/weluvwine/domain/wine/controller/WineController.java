package com.example.weluvwine.domain.wine.controller;

import com.example.weluvwine.domain.wine.service.WineService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "WineController", description = "와인 조회 관련 API")
public class WineController {
    private final WineService wineService;

    @Operation(summary = "검색 메서드", description = "검색 메서드입니다.")
    @GetMapping("/api/search")
    public ResponseEntity<Message> searchWine(@RequestParam String searchKeyword, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.searchWine(searchKeyword);
    }
    @Operation(summary = "와인상세조회 메서드", description = "와인상세조회 메서드입니다.")
    @GetMapping("/api/review/{review-id}")
    public ResponseEntity<Message> readWine(@PathVariable(name = "review-id") Long reviewId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.readWine(reviewId);
    }
    @Operation(summary = "와인랭킹 조회 메서드", description = "와인랭킹 조회 메서드입니다.")
    @GetMapping("/api/rank")
    public ResponseEntity<Message> rankWine(){
        return wineService.rankWine();
    }
}
