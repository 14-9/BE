package com.example.weluvwine.domain.wine.controller;

import com.example.weluvwine.domain.wine.entity.WineType;
import com.example.weluvwine.domain.wine.service.WineService;
import com.example.weluvwine.security.auth.UserDetailsImpl;
import com.example.weluvwine.util.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wine")
@Tag(name = "WineController", description = "와인 조회 관련 API")
public class WineController {
    private final WineService wineService;

    @Operation(summary = "검색 메서드", description = "검색 메서드입니다.")
    @GetMapping("/search")
    public ResponseEntity<Message> searchWine(@RequestParam String searchKeyword, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.searchWine(searchKeyword);
    }

    @Operation(summary = "검색페이지 조회 메서드" , description = "검색창 실행시 와인 정보입니다.")
    @GetMapping("/search/read")
    public ResponseEntity<Message> searchRead(){
      return wineService.searchRead();
    }

    @Operation(summary = "와인상세조회 메서드", description = "와인상세조회 메서드입니다.")
    @GetMapping("/review/{wine-id}")
    public ResponseEntity<Message> readWine(@PathVariable(name = "wine-id") Long wineId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return wineService.readWine(wineId, userDetails.getMember().getId());
    }

    @Operation(summary = "와인랭킹 조회 메서드", description = "와인랭킹 조회 메서드입니다.")
    @GetMapping("/rank")
    public ResponseEntity<Message> rankWine(){
        return wineService.rankWine();
    }
  
    @Operation(summary = "카테고리 조회 메서드" , description = "검색창 실행시 와인 정보입니다.")
    @GetMapping("/search/style")
    public ResponseEntity<Message>searchStyle(WineType wineType){
        return wineService.searchStyle(wineType);
    }

}
