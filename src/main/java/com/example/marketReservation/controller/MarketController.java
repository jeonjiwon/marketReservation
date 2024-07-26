package com.example.marketReservation.controller;

import com.example.marketReservation.model.Auth;
import com.example.marketReservation.model.Market;
import com.example.marketReservation.service.MarketService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    /*
     * 매장 등록
     * - 매장명, 위치, 설명, 관리자 아이디 저장
     * - 파트너 회원 가입여부 (member 사용자 구분 Admin 일때만 가능)
     */
    @Transactional
    @PostMapping("/create/market")
    public void createMarket(@RequestBody Market market) {
        this.marketService.createMarket(market);
    }


    /*
     * 매장 수정
     * - ID 를 찾아서 매장명, 위치, 설명, 관리자 아이디 수정
     */
    @Transactional
    @PutMapping("/update/market")
    public ResponseEntity<?> updateMarket(@RequestBody Market market) {
        var result = this.marketService.updateMarket(market);
        return ResponseEntity.ok(result);
    }

    /*
     * 매장 삭제
     * - ID를 입력 받아 해당 매장 삭제
     */
    @Transactional
    @DeleteMapping("/delete/market")
    public ResponseEntity<?> deleteMarket(@RequestParam("id") Long id) {
        var result = this.marketService.deleteMarket(id);
        return ResponseEntity.ok(result);
    }

    /*
     * 매장 검색
     * - 가나다순, 별점순, 거리순
     */
    @GetMapping("/read/market")
    public ResponseEntity<?> readMarket(@RequestParam("searchGubun") String searchGubun) {
        var result = this.marketService.readMarket(searchGubun);
        return ResponseEntity.ok(result);
    }

    /*
     * 매장 상세 조회
     */
    @GetMapping("/read/marketDetail")
    public ResponseEntity<?> readMarketDtl(@RequestParam("id") Long id) {
        var result = this.marketService.readMarketDtl(id);
        return ResponseEntity.ok(result);
    }
}
