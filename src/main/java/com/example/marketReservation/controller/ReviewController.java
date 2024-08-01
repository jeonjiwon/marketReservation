package com.example.marketReservation.controller;

import com.example.marketReservation.model.Review;
import com.example.marketReservation.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /*
     * 리뷰등록
     * - 작성자만 가능
     * - 예약 이용 후 리뷰 작성 기능 구현(예약자인지 확인 및 작성)
     */
    @Transactional
    @PostMapping("/create/review")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> createReview(@RequestBody Review review) {
        var result = this.reviewService.createReview(review);
        return ResponseEntity.ok(result);
    }


    /*
     * 리뷰 수정
     * - 작성자만 가능
     */
    @Transactional
    @PutMapping("/update/review")
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> updateReview(@RequestBody Review review) {
        var result = this.reviewService.updateReview(review);
        return ResponseEntity.ok(result);
    }

    /*
     * 리뷰 삭제
     * - 작성자, 관리자만 가능
     */
    @Transactional
    @DeleteMapping("/delete/review")
    @PreAuthorize("hasRole('READ') or hasRole('WRITE')")
    public ResponseEntity<?> deleteReview(@RequestParam("id") Long id) {
        var result = this.reviewService.deleteReview(id);
        return ResponseEntity.ok(result);
    }

}
