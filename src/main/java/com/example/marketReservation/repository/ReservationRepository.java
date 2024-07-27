package com.example.marketReservation.repository;

import com.example.marketReservation.domain.ReservationEntity;
import com.example.marketReservation.type.ReservationState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByReservationDtBetweenAndStoreId(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay,
            Long storeId
    );

    boolean existsByReservationDtBetween(
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );

    boolean existsByStoreIdAndReservationStateIn(
            Long storeId,
            List<Integer> reservationStates
    );

}
