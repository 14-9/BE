package com.example.weluvwine.wine.service;

import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import com.example.weluvwine.wine.entity.Wine;
import com.example.weluvwine.wine.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.weluvwine.exception.ErrorCode.WINE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class WineService {
    private final WineRepository wineRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> searchWine(String searchKeyword){
        List<Wine> wineList = wineRepository.findByNameContaining(searchKeyword);
        Message message;
        if(wineList.size() == 0){
            message = Message.setSuccess(StatusEnum.OK,"검색 결과 없음", wineList);
        } else {
            message = Message.setSuccess(StatusEnum.OK,"검색 성공", wineList);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> readWine(Long reviewId){
        Wine wine = wineRepository.findById(reviewId).orElseThrow(
                () -> new CustomException(WINE_NOT_FOUND)
        );
        Message message = Message.setSuccess(StatusEnum.OK,"상세 페이지 조회 성공", wine);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> rankWine(){
        List<Wine> topWineList = wineRepository.findTop8ByRecommendCount(8);
        Message message = Message.setSuccess(StatusEnum.OK,"랭킹 조회 성공", topWineList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
