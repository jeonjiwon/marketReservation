package com.example.marketReservation.controller;

import com.example.marketReservation.model.Reservation;
import com.example.marketReservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /*
     * [관리자] 상점 예약 확인
     * - 날짜, 회원 아이디를 입력 받아
     *   조회하는 일자의 접속한 사용자가 관리하는 상점의 예약내역 조회
     */
    @Transactional(readOnly = true)
    @GetMapping("/read/reservation")
    public ResponseEntity<?> readReservation(
            @RequestParam("searchDate") String searchDate,
            @RequestParam("id") Long id) {
        var result = this.reservationService.readReservation(searchDate, id);
        return ResponseEntity.ok(result);
    }

    /*
     * [고객] 상점 예약 요청
     * - 상점, 날짜, 시간, 내 정보(전화번호) 전송
     * - 예약 후에 변경은 불가하며, 취소 후 재진행 하도록 구현
     */
    @Transactional
    @PostMapping("/create/reservation")
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
       var result = this.reservationService.createReservation(reservation);
        return ResponseEntity.ok(result);
    }

    /*
     * [고객] 상점 예약 취소
     * - 예약번호를 통해 해당 데이터 취소상태로 업데이트 처리
     */
    @Transactional
    @GetMapping("/cancel/reservation")
    public ResponseEntity<?> cancelReservation(@RequestParam("id") Long id) {
        var result = this.reservationService.cancelReservation(id);
        return ResponseEntity.ok(result);
    }

    /*
    * 키오스크 통해 전달 받은 고객 도착 확인 기능
    */

}
