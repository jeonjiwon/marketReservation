package com.example.marketReservation.service;

import com.example.marketReservation.MarketReservationApplication;
import com.example.marketReservation.domain.MarketEntity;
import com.example.marketReservation.domain.MemberEntity;
import com.example.marketReservation.model.Market;
import com.example.marketReservation.repository.MarketRepository;
import com.example.marketReservation.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MarketService {

    final private MarketRepository marketRepository;

    final private MemberRepository memberRepository;

    final private Logger logger = LoggerFactory.getLogger(MarketReservationApplication.class);

    public MarketService(MarketRepository marketRepository, MemberRepository memberRepository) {
        this.marketRepository = marketRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createMarket(Market market){
        logger.info("started to create market");

        // adminId 가 유효한 아이디인지 체크
        MemberEntity member = memberRepository.findById(market.getAdminId())
                .orElseThrow(()-> new RuntimeException("관리자 정보를 찾을 수 없습니다. "));

        if (!"A".equals(member.getUserFgCd())) {
            throw new RuntimeException("매장 파트너 회원의 경우에만 상점등록 가능합니다.");
        }

        // 저장
        MarketEntity newMarket = new MarketEntity();
        newMarket.setName(market.getName());
        newMarket.setLocation(market.getLocation());
        newMarket.setDescription(market.getDescription());
        newMarket.setDeleteYn("N");
        newMarket.setAdminId(member);
        marketRepository.save(newMarket);
        logger.info("end to create market");

    }

}
