package com.joolove.core.service;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.repository.jpa.query.GoodsRepository;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;

    // 상품 1개 상세 조회
    public GoodsViewDetails findGoodsDetail(String goodsName) {
        return goodsRepository.findGoodsDetailByName(goodsName);
    }

    // 상품 n개 조회 (이름, 카테고리 별)
    public List<IGoodsView> findGoodsList(String goodsName, String type,
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
            pagingInfo = PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.ASC, "name"));
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

    // 사용자별 추천 리스트
    public List<IGoodsView> getRecommendationGoodsList(String abvLimit, List<String> typeOrLabel, UserRecommendationBase.EFigure sweetness, List<String> goodsNameList) {
        int defaultPage = 0;
        int defaultSize = 10;
        Pageable pagingInfo = PageRequest.of(defaultPage, defaultSize);

        if (sweetness == UserRecommendationBase.EFigure.HIGH) {
            pagingInfo = PageRequest.of(defaultPage, defaultSize, Sort.by(Sort.Direction.DESC, "opt5Value"));
        } else if (sweetness == UserRecommendationBase.EFigure.LOW) {
            pagingInfo = PageRequest.of(defaultPage, defaultSize, Sort.by(Sort.Direction.ASC, "opt5Value"));
        }

        List<IGoodsView> iGoodsViewList;
        String typeOrLabelStr = null;
        if (typeOrLabel != null && !typeOrLabel.isEmpty()) {
            typeOrLabelStr = "'" + String.join("','", typeOrLabel) + "'";    // 예) '위스키','와인','베스트'
        }

        /*
        MySQL 에서는 in like 를 동시에 사용할 수 없다.
        따라서 아래 예처럼 검색하고 싶으면 FTS 등 다른 방식을 고려해야한다.
        예) '%위스키%','%와인%','%베스트%'
        */

        if (goodsNameList == null || goodsNameList.isEmpty()) {
            if (StringUtils.isBlank(typeOrLabelStr)) {
                iGoodsViewList = goodsRepository.findRecommendationGoodsList(abvLimit, pagingInfo);
            } else {
                iGoodsViewList = goodsRepository.findRecommendationGoodsList(abvLimit, typeOrLabelStr, pagingInfo);
            }
        } else {
            String goodsName = String.join(" ", goodsNameList);     // 예) '짐빔 그라몰랑 세이비어'

            if (StringUtils.isBlank(typeOrLabelStr)) {
                iGoodsViewList = goodsRepository.findRecommendationGoodsListByGoodsNameList(abvLimit, goodsName, pagingInfo);
            } else {
                iGoodsViewList = goodsRepository.findRecommendationGoodsListByGoodsNameList(abvLimit, typeOrLabelStr, goodsName, pagingInfo);
            }
        }

        return iGoodsViewList.size() > 100 ? iGoodsViewList.subList(0, 100) : iGoodsViewList;
    }

    public List<IGoodsView> findNewGoodsListDefault() {
        return findNewGoodsList(null, null);
    }

    // 상품 n개 조회 (신상품)
    public List<IGoodsView> findNewGoodsList(Integer page, Integer size) {
        int defaultPage = 0;
        int defaultSize = 10;
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;

        return goodsRepository.findNewGoodsList(
                PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "createdDate")));
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
