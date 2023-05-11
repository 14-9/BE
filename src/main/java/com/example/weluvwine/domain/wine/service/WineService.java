package com.example.weluvwine.domain.wine.service;



import com.example.weluvwine.domain.member.entity.Member;
import com.example.weluvwine.domain.member.repository.MemberRepository;
import com.example.weluvwine.domain.recommend.repository.RecommendRepository;
import com.example.weluvwine.domain.review.dto.ReviewListResponseDto;
import com.example.weluvwine.domain.review.repository.ReviewRepository;
import com.example.weluvwine.domain.wine.dto.WineDetailPageResponseDto;
import com.example.weluvwine.domain.wine.entity.Wine;
import com.example.weluvwine.domain.wine.entity.WineType;
import com.example.weluvwine.domain.wine.repository.WineRepository;
import com.example.weluvwine.exception.CustomException;
import com.example.weluvwine.util.Message;
import com.example.weluvwine.util.StatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.weluvwine.exception.ErrorCode.USER_NOT_FOUND;
import static com.example.weluvwine.exception.ErrorCode.WINE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class WineService {
    private final WineRepository wineRepository;
    private final ReviewRepository reviewRepository;
    private final RecommendRepository recommendRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<Message> searchWine(String searchKeyword){
        if (searchKeyword.isEmpty()) {
            Message message = Message.setSuccess(StatusEnum.OK,"검색 결과 없음");
            return new ResponseEntity<>(message, HttpStatus.OK);
        }
        List<Wine> wineList = wineRepository.findByNameContaining(searchKeyword);
        Message message;
        if(wineList.size() == 0){
            message = Message.setSuccess(StatusEnum.OK,"검색 결과 없음");
        } else {
            message = Message.setSuccess(StatusEnum.OK,"검색 성공", wineList);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> readWine(Long wineId, Long memberId){
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new CustomException(WINE_NOT_FOUND)
        );
        List<ReviewListResponseDto> reviewListResponseDto = reviewRepository.findAllByWineIdOrderByCreatedAtDesc(wineId)
                .stream().map(ReviewListResponseDto::new).collect(Collectors.toList());
        boolean recommended = recommendRepository.findByWineIdAndMemberId(wineId, memberId).isPresent();
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        WineDetailPageResponseDto dto = new WineDetailPageResponseDto(wine, recommended, member, reviewListResponseDto);
        Message message = Message.setSuccess(StatusEnum.OK,"상세 페이지 조회 성공", dto);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> rankWine(){
        List<Wine> topWineList = wineRepository.findTop8ByOrderByRecommendCountDesc();

        Message message = Message.setSuccess(StatusEnum.OK,"랭킹 조회 성공", topWineList);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<Message> searchRead(){
        List<Wine> vintageList = wineRepository.findTop8ByOrderByVintageAsc();
        Message message = Message.setSuccess(StatusEnum.OK,"검색 페이지 조회 성공",vintageList);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message>searchStyle(WineType wineType){
        List<Wine> CateList = wineRepository.findTop8ByTypeOrderByVintageAsc(wineType);
        Message message = Message.setSuccess(StatusEnum.OK,"카테고리 조회 성공",CateList);
        return new ResponseEntity<>(message,HttpStatus.OK);
    }
}
