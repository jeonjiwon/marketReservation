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
     * - 매장명, 위치, 설명 저장
     * - 파트너 회원 가입여부 (member 사용자 구분 Admin 일때만 가능)
     */
    @Transactional
    @PostMapping("/create/market")
    public void createMarket(@RequestBody Market market) {
        this.marketService.createMarket(market);
    }


//    /*
//     * 매장 수정
//     * - ID 를 찾아서
//     */
//    @Transactional
//    @PutMapping("/update/market")
//    public ResponseEntity<?> updateMarket(
//            @RequestParam("id")             String id,
//            @RequestParam("name")           String name,
//            @RequestParam("location")       String location,
//            @RequestParam("description")    String description
//    ) {
//        var result = this.marketService.signUp(request);
//        return ResponseEntity.ok(request);
//    }
//
//    /*
//     * 매장 삭제
//     */
//    @Transactional
//    @DeleteMapping("/delete/market")
//    public ResponseEntity<?> deleteMarket(@RequestParam("id") String id) {
//        var result = this.marketService.signUp(request);
//        return ResponseEntity.ok(request);
//    }
//
//    /*
//     * 매장 검색
//     */
//    @GetMapping("/read/market")
//    public ResponseEntity<?> readMarket(@RequestBody Auth.SignUp request) {
//        var result = this.marketService.signUp(request);
//        return ResponseEntity.ok(request);
//    }
//
//    /*
//     * 매장 상세 조회
//     */
//    @GetMapping("/read/marketDetail")
//    public ResponseEntity<?> readMarketDtl(@RequestParam("id") String id) {
//        var result = this.marketService.signUp(request);
//        return ResponseEntity.ok(request);
//    }
}
