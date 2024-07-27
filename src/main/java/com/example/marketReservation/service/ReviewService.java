package com.example.marketReservation.service;

import com.example.marketReservation.MarketReservationApplication;
import com.example.marketReservation.domain.MarketEntity;
import com.example.marketReservation.domain.MemberEntity;
import com.example.marketReservation.domain.ReservationEntity;
import com.example.marketReservation.domain.ReviewEntity;
import com.example.marketReservation.model.Reservation;
import com.example.marketReservation.model.Review;
import com.example.marketReservation.repository.MarketRepository;
import com.example.marketReservation.repository.MemberRepository;
import com.example.marketReservation.repository.ReservationRepository;
import com.example.marketReservation.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    final private ReviewRepository reviewRepository;

    final private ReservationRepository reservationRepository;

    final private MarketRepository marketRepository;

    final private MemberRepository memberRepository;

    public ReviewService(ReviewRepository reviewRepository, ReservationRepository reservationRepository, MarketRepository marketRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.marketRepository = marketRepository;
        this.memberRepository = memberRepository;
    }

    final private Logger logger = LoggerFactory.getLogger(MarketReservationApplication.class);


    @Transactional
    public ReviewEntity createReview(Review review){
        logger.info("started to create review");

        // 회원가입 되어있는 유저인지 확인
        MemberEntity member = memberRepository.findById(review.getRegisterUserId())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다. "));

        // 상점 정보가 유효한지 확인
        MarketEntity market = marketRepository.findById(review.getStoreId())
                .orElseThrow(()-> new RuntimeException("해당 상점을 찾을 수 없어 리뷰 등록 불가합니다. "));

        // 등록하고자 하는 상점을 이용한 이력이 있는지 확인
        List<Integer> reservationStates = new ArrayList<>();
        reservationStates.add(2);
        boolean existsReservation = reservationRepository.existsByStoreIdAndReservationStateIn(
                review.getStoreId(),
                reservationStates
        );
        if(!existsReservation) {
            throw new RuntimeException("해당 상점을 이용한 사용자만 리뷰 등록 가능합니다. ");
        }

        // 저장
        ReviewEntity newReview = new ReviewEntity();
        newReview.setRegisterDt(review.getRegisterDt());
        newReview.setRegisterUserId(review.getRegisterUserId());
        newReview.setStoreId(review.getStoreId());
        newReview.setContent(review.getContent());
        newReview.setRating(review.getRating());
        reviewRepository.save(newReview);
        logger.info("end to create review");
        return newReview;

    }

    @Transactional
    public ReviewEntity updateReview(Review review){
        logger.info("started to update review");
        ReviewEntity updateReview = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 글 입니다. "));

        updateReview.setRegisterDt(review.getRegisterDt());
        updateReview.setRegisterUserId(review.getRegisterUserId());
        updateReview.setStoreId(review.getStoreId());
        updateReview.setContent(review.getContent());
        updateReview.setRating(review.getRating());
        reviewRepository.save(updateReview);
        logger.info("end to update market");
        return updateReview;
    }

    @Transactional
    public ReviewEntity deleteReview(Long id) {
        logger.info("started to delete review");
        ReviewEntity delete = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 글 입니다. "));
        reviewRepository.deleteById(delete.getId());
        logger.info("end to delete review");
        return delete;
    }

}
