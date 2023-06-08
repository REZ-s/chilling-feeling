package com.joolove.core.security.service;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.domain.product.GoodsStats;
import com.joolove.core.dto.query.GoodsView;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.repository.GoodsDetailsRepository;
import com.joolove.core.repository.GoodsRepository;
import com.joolove.core.repository.GoodsStatsRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsStatsRepository goodsStatsRepository;

    // 상품 상세 화면을 위한 조회
    /*
    public GoodsViewDetails getGoodsViewDetails(String query) {
    }*/

    // 상품 썸네일을 위한 조회
    /*
    public List<GoodsView> getGoodsViewList(String query) {
        List<GoodsView> goodsViewList = new ArrayList<>();

        this.findGoodsListByPaging(query).forEach(goods -> {
            if (goods.getSalesStatus() != 1) {
                return;
            }

            GoodsDetails goodsDetails = goodsDetailsRepository.findOneById(goods.getGoodsDetails().getId());
            GoodsStats goodsStats = goodsStatsRepository.findOneById(goods.getGoodsStats().getId());
            if (goodsDetails == null || goodsStats == null) {
                return;
            }

            String type = goodsDetails.getType();
            String name = goodsDetails.getName();
            String imageUrl = goodsDetails.getImageUrl();
            String label = goodsStats.getLabel();
            Integer reviewCount = goodsStats.getReviewCount();
            String score = goodsStats.getScore();

            GoodsView goodsView = GoodsView.builder()
                    .type(type)
                    .name(name)
                    .imageUrl(imageUrl)
                    .label(label)
                    .reviewCount(reviewCount)
                    .score(score)
                    .build();

            goodsViewList.add(goodsView);
        });

        return goodsViewList;
    }*/

    // 상품 1개 상세 조회
    public GoodsViewDetails findGoodsDetail(String goodsName) {
        return goodsRepository.findGoodsDetailByName(goodsName);
    }

    // 상품 n개 조회 (이름, 카테고리 별)
    public List<GoodsView> findGoodsListByPaging(String goodsName,
                                                 String type,
                                                 Integer page, Integer size, String sort) {
        int defaultPage = 0;
        int defaultSize = 10;
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;
        Pageable pagingInfo;

        if (StringUtils.isBlank(sort)) {
            pagingInfo = PageRequest.of(requestedPage, requestedSize);
        } else {
            // 현재 이름 기준으로 정렬 지원. (추후에 신상품, 베스트셀러, 리뷰숫자, 고득점 기준 정렬 추가 개발 예정)
            pagingInfo = PageRequest.of(requestedPage, requestedSize, Sort.by("name").ascending());
        }

        if (StringUtils.isBlank(goodsName)) {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return goodsRepository.findGoodsList(pagingInfo);
            } else {
                return goodsRepository.findGoodsListByType(type, pagingInfo);
            }
        } else {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return goodsRepository.findGoodsListByName(goodsName, pagingInfo);
            } else {
                return goodsRepository.findGoodsListByNameAndType(goodsName, type, pagingInfo);
            }
        }
    }

    // 상품 1개 조회 (이름)
    public Goods findOneByGoodsName(String goodsName) {
        return goodsRepository.findOneByName(goodsName).orElse(null);
    }

    // 가격 변경
    @Transactional
    public void changePrice(String goodsName, Integer price) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changePrice(price);
        }
    }

    // 재고 변경
    @Transactional
    public void changeStock(String goodsName, Integer stock) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeStock(stock);
        }
    }

    // 재고 1개 추가
    @Transactional
    public void increaseStock(String goodsName) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.increaseStock();
        }
    }

    // 판매량 변경
    @Transactional
    public void changeSalesFigures(String goodsName, Long salesFigures) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeSalesFigures(salesFigures);
        }
    }

    // 판매량 1개 추가
    @Transactional
    public void increaseSalesFigures(String goodsName) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.increaseSalesFigures();
        }
    }

    // 판매 상태 변경
    @Transactional
    public void changeSalesStatus(String goodsName, Short salesStatus) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeSalesStatus(salesStatus);
        }
    }

    // 상품 1개 추가
    @Transactional
    public void addGoods(Goods goods) {
        // UTC 기준으로 생성 시간을 기록
        // 우리나라 기준으로 바꿀 필요가 있겠음
        goodsRepository.save(goods);
    }

    // 상품 여러개 추가
    @Transactional
    public void addGoodsList(List<Goods> goodsList) {
        goodsRepository.saveAll(goodsList);
    }
}
