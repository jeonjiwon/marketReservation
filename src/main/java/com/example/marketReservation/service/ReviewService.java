package com.example.marketReservation.service;

import com.example.marketReservation.MarketReservationApplication;
import com.example.marketReservation.domain.MarketEntity;
import com.example.marketReservation.domain.MemberEntity;
import com.example.marketReservation.domain.ReviewEntity;
import com.example.marketReservation.model.Review;
import com.example.marketReservation.repository.MarketRepository;
import com.example.marketReservation.repository.MemberRepository;
import com.example.marketReservation.repository.ReservationRepository;
import com.example.marketReservation.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService {
    final private ReviewRepository reviewRepository;

    final private ReservationRepository reservationRepository;

    final private MarketRepository marketRepository;

    final private MemberRepository memberRepository;

    final private UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
                         ReservationRepository reservationRepository,
                         MarketRepository marketRepository,
                         MemberRepository memberRepository,
                         UserService userService)
    {
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.marketRepository = marketRepository;
        this.memberRepository = memberRepository;
        this.userService = userService;
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
                .orElseThrow(()-> new RuntimeException("상점 정보를 찾을 수 없습니다. "));

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

        // 해당 상점 정보를 찾아서 평균 상점 정보 업데이트
        market.addAverRating(review.getRating() == null ? 0L : review.getRating(),
                             market.getRatingCount() == null ? 0L : market.getRatingCount());



        return newReview;
    }

    @Transactional
    public ReviewEntity updateReview(Review review){
        logger.info("started to update review");

        ReviewEntity writtenReview = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 글 입니다. "));

        // 상점 정보가 유효한지 확인
        MarketEntity market = marketRepository.findById(writtenReview.getStoreId())
                .orElseThrow(()-> new RuntimeException("상점 정보를 찾을 수 없습니다. "));

        // 작성자가 수정하려고 하는지 확인
        MemberEntity member = memberRepository.findById(writtenReview.getRegisterUserId())
                .orElseThrow(() -> new RuntimeException("사용자 정보를 찾을 수 없습니다. [작성자만 수정가능합니다.]"));


        // 기존 평점 삭제처리
        market.deleteAverRating(writtenReview.getRating(), market.getRatingCount());

        writtenReview.setContent(review.getContent());
        writtenReview.setRating(review.getRating());
        reviewRepository.save(writtenReview);

        // 신규로 변경할 평점 정보 업데이트
        market.addAverRating(writtenReview.getRating(), market.getRatingCount());
        logger.info("end to update market");

        return writtenReview;
    }

    @Transactional
    public ReviewEntity deleteReview(Long id) {
        logger.info("started to delete review");

        ReviewEntity delete = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 리뷰 글 입니다. "));

        // 상점 정보가 유효한지 확인
        MarketEntity market = marketRepository.findById(delete.getStoreId())
                .orElseThrow(()-> new RuntimeException("상점 정보를 찾을 수 없습니다. "));


        // 작성자 또는 관리자가 수정하려고 하는지 확인
        MemberEntity member = memberRepository.findByUsername(userService.getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
        if(!member.getId().equals(delete.getRegisterUserId()) && !member.getId().equals(market.getAdminId())) {
            throw new RuntimeException("해당 게시물 삭제 권한이 없습니다." );
        }

        reviewRepository.deleteById(delete.getId());
        market.deleteAverRating(delete.getRating() == null ? 0L : delete.getRating(),
                market.getRatingCount() == null ? 0L : market.getRatingCount());

        logger.info("end to delete review");
        return delete;
    }

}
