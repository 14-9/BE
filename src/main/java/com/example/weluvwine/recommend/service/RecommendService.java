package com.example.weluvwine.recommend.service;

import com.example.weluvwine.member.entity.Member;
import com.example.weluvwine.recommend.entity.Recommend;
import com.example.weluvwine.recommend.repository.RecommendRepository;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import com.example.weluvwine.wine.entity.Wine;
import com.example.weluvwine.wine.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendService {
    private final WineRepository wineRepository;
    private final RecommendRepository recommendRepository;

    public ResponseEntity<Message> recommendWine(Long wineId, Member member) {
        Wine wine = findWineById(wineId);
        Optional<Recommend> findRecommend = recommendRepository.findByWineIdAndMemberId(wineId,member.getId());
        if(findRecommend.isPresent()){
            Recommend recommend = findRecommend.get();
            recommendRepository.delete(recommend);
            wine.setRecommendCount(wine.getRecommendCount()-1);
            Message message = Message.setSuccess(StatusEnum.OK, "추천을 취소하였습니다.",null);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } else {
            Recommend recommend = new Recommend(wine, member);
            recommendRepository.save(recommend);
            wine.setRecommendCount(wine.getRecommendCount()+1);
            Message message = Message.setSuccess(StatusEnum.OK, "추천 성공!",null);
            return new ResponseEntity<>(message, HttpStatus.OK);
        }

    }

    public Wine findWineById(Long id) {
        return wineRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 와인이 존재하지 않습니다."));
    }

}
