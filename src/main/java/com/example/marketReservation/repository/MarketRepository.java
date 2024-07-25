package com.example.marketReservation.repository;

import com.example.marketReservation.domain.MarketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<MarketEntity, Long> {

//    Optional<MarketEntity> findById(String id);

}
