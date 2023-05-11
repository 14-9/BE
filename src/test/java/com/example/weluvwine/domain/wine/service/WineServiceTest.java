package com.example.weluvwine.domain.wine.service;

import com.example.weluvwine.domain.recommend.repository.RecommendRepository;
import com.example.weluvwine.domain.review.repository.ReviewRepository;
import com.example.weluvwine.domain.wine.entity.Wine;
import com.example.weluvwine.domain.wine.entity.WineType;
import com.example.weluvwine.domain.wine.repository.WineRepository;
import com.example.weluvwine.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
public class WineServiceTest {

    @Autowired
    private WineService wineService;

    @Autowired
    private WineRepository wineRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private RecommendRepository recommendRepository;

    private Wine wine1;
    private Wine wine2;

    @BeforeEach
    void setUp() {
        wineService = new WineService(wineRepository,reviewRepository,recommendRepository);
        wine1 = new Wine(
                1L
                ,"https://www.wineok.com"
                ,"와인테스트"
                ,"Chile (칠레) > Other (기타 지역)"
                ,"San Pedro / 산 페드로"
                ,"2013"
                ,"Sauvignon Blanc / 소비뇽 블랑"
                , WineType.WHITE
                ,"합리적인 가격에 최고의 리티 었다."
                ,0);
        wine2 = new Wine(
                2L
                ,"https://www.wineok.com"
                ,"제발그만해"
                ,"Chile (칠레) > Other (기타 지역)"
                ,"San Pedro / 산 페드로"
                ,"2013"
                ,"Sauvignon Blanc / 소비뇽 블랑"
                , WineType.WHITE
                ,"합리적인 가격에 최고의 리티 었다."
                ,0);
        wineRepository.save(wine1);
        wineRepository.save(wine2);
    }
    @Nested
    @DisplayName("Search Wine_Test")
    class Search{

        @Test
        @DisplayName("No Search")
        void noSearchWine() {
            // Given
            String searchKeyword = "ㅅㄷㄴㅅ";
            // When
            ResponseEntity<Message> response = wineService.searchWine(searchKeyword);
            // Then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getMessage()).isEqualTo("검색 결과 없음");
        }
        @Test
        @DisplayName("Search !!")//두가지중 하나의 제목만 있어도 검색가능
        void searchWine(){
            String searchKeyword = "";

            ResponseEntity<Message> response = wineService.searchWine(searchKeyword);
            assertEquals(HttpStatus.OK, response.getStatusCode());

            List<Wine> wineList = (List<Wine>) response.getBody().getData();

            //와인리스트가 비어있지 않은지 확인
            assertThat(wineList).isNotEmpty();
            //검색어를 포함하는 와인이 있는지 확인
            assertThat(wineList).anyMatch(wine -> wine.getName().contains(searchKeyword));
            //상태 코드가 HttpStatus.OK인지 확인
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            //메시지가 "검색 성공"인지 확인
            assertThat(response.getBody().getMessage()).isEqualTo("검색 성공");
        }
        @Test
        @DisplayName("emptyKeyword_Search !!")
        void emptyKeyword_SearchWine(){
            String searchKeyword = "";

            ResponseEntity<Message> response  = wineService.searchWine(searchKeyword);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getMessage()).isEqualTo("검색 결과 없음");
        }





    }


}
