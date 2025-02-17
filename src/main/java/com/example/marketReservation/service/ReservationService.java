package com.example.marketReservation.service;

import com.example.marketReservation.MarketReservationApplication;
import com.example.marketReservation.domain.MarketEntity;
import com.example.marketReservation.domain.MemberEntity;
import com.example.marketReservation.domain.ReservationEntity;
import com.example.marketReservation.model.Reservation;
import com.example.marketReservation.repository.MarketRepository;
import com.example.marketReservation.repository.MemberRepository;
import com.example.marketReservation.repository.ReservationRepository;
import com.example.marketReservation.type.ReservationState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    final private ReservationRepository reservationRepository;

    final private MarketRepository marketRepository;

    final private MemberRepository memberRepository;

    final private Logger logger = LoggerFactory.getLogger(MarketReservationApplication.class);

    public ReservationService(ReservationRepository reservationRepository,
                              MarketRepository marketRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.marketRepository = marketRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationEntity> readReservation(String searchDate, Long id){
        MemberEntity member = memberRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("사용자 정보를 찾을 수 없습니다."));

        MarketEntity market = marketRepository.findByAdminId(member.getId())
                .orElseThrow(()-> new RuntimeException("매장 관리자로 등록된 회원만 예약내역 조회가능합니다."));

        LocalDate date = LocalDate.parse(searchDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDateTime startOfDay = date.atStartOfDay(); // 날짜의 시작 (00:00:00)
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 날짜의 끝 (23:59:59.999999999)

        // 해당
        return reservationRepository.findByReservationDtBetweenAndStoreId(
                startOfDay,
                endOfDay,
                market.getId()
        );
    }

    @Transactional
    public ReservationEntity createReservation(Reservation reservation){
        logger.info("started to create reservation");

        // 유효한 예약자인지 체크
        MemberEntity member = memberRepository.findById(reservation.getUserId())
                .orElseThrow(()-> new RuntimeException("예약자 정보를 찾을 수 없습니다. 회원 가입 후 진행바랍니다. "));

        // 유효한 상점인지 체크
        MarketEntity market = marketRepository.findById(reservation.getStoreId())
                .orElseThrow(()-> new RuntimeException("해당 상점을 찾을 수 없습니다. "));

        List<Integer> reservationStates = new ArrayList<>();
        reservationStates.add(0); //예약요청
        reservationStates.add(1); //도착
        reservationStates.add(2); //완료
        reservationStates.add(5); //승인

        boolean exists = reservationRepository.existsByReservationDtBetweenAndStoreIdAndReservationStateIn(
                reservation.getReservationDt(),
                reservation.getReservationDt(),
                market.getId(),
                reservationStates
        );
        if(exists) {
            throw new RuntimeException("해당 일정은 이미 예약등록 마감되었습니다. ");
        }

        ReservationEntity createReservation = new ReservationEntity();
        createReservation.setReservationState(ReservationState.REQUESTED);
        createReservation.setReservationDt(reservation.getReservationDt());
        createReservation.setStoreId(reservation.getStoreId());
        createReservation.setUserId(reservation.getUserId());
        createReservation.setPhoneNumber(member.getPhoneNumber());
        createReservation.setRmkDc(reservation.getRmkDc());
        reservationRepository.save(createReservation);

        logger.info("end to create reservation");
        return createReservation;

    }

    @Transactional
    public ReservationEntity cancelReservation(Long id){
        logger.info("started to cancel reservation");
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 예약번호 입니다."));

        if(reservation.getReservationState() != ReservationState.REQUESTED ) {
            throw new RuntimeException("예약 요청 상태에서만 취소 가능합니다. ");
        }

        reservation.setReservationState(ReservationState.CANCELED);
        reservationRepository.save(reservation);
        logger.info("end to cancel reservation");
        return reservation;
    }

    @Transactional
    public ReservationEntity arrivedCheck(Long id){
        logger.info("started to arrived check");
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 예약번호 입니다."));

        if(reservation.getReservationState() != ReservationState.APPROVAL) {
            throw new RuntimeException("매장 승인 후 도착확인 처리 가능합니다. ");
        }

        reservation.setReservationState(ReservationState.ARRIVED);
        reservationRepository.save(reservation);
        logger.info("end to arrived check");
        return reservation;
    }

    @Transactional
    public ReservationEntity reservationState(Long id, boolean appYn){
        logger.info("started to reservationState");
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("존재하지 않는 예약번호 입니다."));

        if(appYn) {
            reservation.setReservationState(ReservationState.APPROVAL);
        } else {
            reservation.setReservationState(ReservationState.REJECT);
        }

        reservationRepository.save(reservation);
        logger.info("end to reservationState");
        return reservation;
    }
}
