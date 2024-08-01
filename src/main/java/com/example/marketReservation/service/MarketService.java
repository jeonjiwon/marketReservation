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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public MarketEntity createMarket(Market market){
        logger.info("started to create market");

        // adminId 가 유효한 아이디인지 체크
        MemberEntity member = memberRepository.findById(market.getAdminId())
                .orElseThrow(() -> new RuntimeException("관리자 정보를 찾을 수 없습니다. "));

        // 해당 계정으로 이미 등록된 상점 내역이 있다면 불가능 (계정 1개당 매장 1개씩 관리하도록 처리)
        if(marketRepository.existsByAdminId(member.getId())){
            throw new RuntimeException("이미 타 상점 관리자입니다. ");
        }

        // 저장
        MarketEntity newMarket = new MarketEntity();
        newMarket.setName(market.getName());
        newMarket.setLocation(market.getLocation());
        newMarket.setDescription(market.getDescription());
        newMarket.setAdminId(member.getId());
//        newMarket.setAdminId(member);
        newMarket.setRating(0.0);
        newMarket.setRatingCount(0L);
        marketRepository.save(newMarket);
        logger.info("end to create market");
        return newMarket;

    }

    @Transactional
    public MarketEntity updateMarket(Market market) {
        logger.info("started to update market");

        // 새로 입력한 adminId 가 유효한 아이디인지 체크
        MemberEntity member = memberRepository.findById(market.getAdminId())
                .orElseThrow(()-> new RuntimeException("관리자 정보를 찾을 수 없습니다. "));

        MarketEntity updateMarket = marketRepository.findById(market.getId())
                .orElseThrow(()-> new RuntimeException("미등록 혹은 이미 삭제 처리된 상점정보 입니다. "));

        updateMarket.setName(market.getName());
        updateMarket.setLocation(market.getLocation());
        updateMarket.setDescription(market.getDescription());
        updateMarket.setAdminId(member.getId());
//        updateMarket.setAdminId(member);
        marketRepository.save(updateMarket);
        logger.info("end to update market");

        return updateMarket;
    }

    @Transactional
    public MarketEntity deleteMarket(Long id) {
        logger.info("started to delete market");
        MarketEntity deleteMarket = marketRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("기존에 등록되지 않은 상점정보 입니다. "));
        marketRepository.deleteById(deleteMarket.getId());
        logger.info("end to delete market");
        return deleteMarket;
    }

    @Transactional(readOnly = true)
    public List<MarketEntity> readMarket(String searchGubun){
        List<MarketEntity> resultList = new ArrayList<>();
        if("1".equals(searchGubun)) {
            // 가나다 순 정렬
            resultList = marketRepository.findAllByOrderByNameAsc();
        } else if ("2".equals(searchGubun)) {
            // 별점 순
            resultList = marketRepository.findAllByOrderByRatingDesc();
        } else {
            // 거리순
            resultList = marketRepository.findAllByOrderByLocationAsc();
        }
        return resultList;
    }

    @Transactional(readOnly = true)
    public MarketEntity readMarketDtl(Long id) {
        MarketEntity market = marketRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("해당 상점 정보를 조회할 수 없습니다."));
        return market;
    }

}
