package com.example.marketReservation.repository;

import com.example.marketReservation.domain.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {

    // 이름으로 오름차순 정렬
    List<MarketEntity> findAllByOrderByNameAsc();

    // 별점으로 내림차순 정렬
    List<MarketEntity> findAllByOrderByRatingDesc();

    // 거리로 오름차순 정렬
    List<MarketEntity> findAllByOrderByLocationAsc();

    Optional<MarketEntity> findByAdminId(Long adminId);

    boolean existsByAdminId(Long adminId);
}
