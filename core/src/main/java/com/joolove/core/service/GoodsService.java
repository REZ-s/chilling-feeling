package com.joolove.core.service;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDiscount;
import com.joolove.core.domain.recommendation.UserRecommendationBase;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.IGoodsViewDetails;
import com.joolove.core.repository.GoodsDetailsRepository;
import com.joolove.core.repository.GoodsDiscountRepository;
import com.joolove.core.repository.query.GoodsRepository;
import com.joolove.core.utils.RedisUtils;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {
    private final GoodsRepository goodsRepository;
    private final GoodsDetailsRepository goodsDetailsRepository;
    private final GoodsDiscountRepository goodsDiscountRepository;
    private final RedisUtils redisUtils;
    
    // 상품(GoodsViewDetails) 1개 조회 (ID)
    public GoodsViewDetails findGoodsByGoodsId(UUID goodsId) {
        return goodsDetailsRepository.findGoodsByGoodsId(goodsId);
    }

    // 상품(Goods) 1개 조회 (ID)
    public Goods findSimpleGoodsByGoodsId(UUID goodsId) {
        return goodsRepository.findById(goodsId).orElse(null);
    }

    // 상품(Goods) 1개 조회 (이름)
    public Goods findSimpleGoodsByGoodsName(String goodsName) {
        return goodsRepository.findOneByName(goodsName).orElse(null);
    }

    // 상품(GoodsViewDetails) 1개 조회 (랜덤)
    @Async("cfAsync")
    public CompletableFuture<IGoodsViewDetails> findGoodsDetailsRandom() {
        return CompletableFuture.completedFuture(goodsRepository.findGoodsDetailsRandom().get(0));
    }

    // 상품(GoodsViewDetails) 1개 조회 (이름)
    public GoodsViewDetails findGoodsDetails(String goodsName) {
        return goodsRepository.findGoodsDetailsByName(goodsName);
    }

    // 상품(GoodsView) 1개 조회 (이름)
    public IGoodsView findGoodsByGoodsName(String goodsName) {
        return goodsRepository.findGoodsByGoodsName(goodsName);
    }

    // 상품(GoodsView) n개 한 번에 조회 (이름)
    public List<IGoodsView> findGoodsListByGoodsName(List<String> goodsNameList) {
        return goodsRepository.findGoodsListByGoodsName(goodsNameList);
    }

    private String generateKeyString(String goodsName, String type, Integer page, Integer size, String sortBy) {
        return "GoodsName_" + goodsName + "_" + type + "_" + page + "_" + size + "_" + sortBy;
    }

    public List<IGoodsView> findGoodsList(String goodsName, String type, Integer page, Integer size, String sortBy)
            throws ExecutionException, InterruptedException {
        String cacheKey = generateKeyString(goodsName, type, page, size, sortBy);

        List<IGoodsView> cachedGoodsList = (List<IGoodsView>) redisUtils.get(cacheKey, ArrayList.class);
        if (cachedGoodsList == null) {
            List<IGoodsView> goodsList = findGoodsListFromDB(goodsName, type, page, size, sortBy).get();
            if (!goodsList.isEmpty()) {
                redisUtils.add(cacheKey, goodsList, 14, TimeUnit.DAYS);
            }

            return goodsList;
        }

        return cachedGoodsList;
    }

    // 상품 개수 반환
    public long getGoodsListCount(String goodsName, String type) {
        if (StringUtils.isBlank(goodsName)) {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return goodsRepository.count();
            } else {
                return goodsRepository.getGoodsListCountByType(type);
            }
        } else {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return goodsRepository.getGoodsListCountByName(goodsName);
            } else {
                return goodsRepository.getGoodsListCountByNameAndType(goodsName, type);
            }
        }
    }

    // 상품(GoodsView) n개 조회 (이름, 카테고리 별)
    @Async("cfAsync")
    public CompletableFuture<List<IGoodsView>> findGoodsListFromDB(String goodsName, String type, Integer page, Integer size, String sortBy) {
        int defaultPage = 0;
        int defaultSize = 10;
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;
        Pageable pagingInfo = PageRequest.of(requestedPage, requestedSize);

        if (sortBy == null) {
            ;
        } else if (sortBy.equals("name")) {     // 상품 이름 기준으로 정렬
            pagingInfo = PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.ASC, "name"));
        } else if (sortBy.equals("old")) {      // 업데이트 날짜를 기준으로 정렬 (오래된순)
            pagingInfo = PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.ASC, "updatedDate"));
        } else if (sortBy.equals("new")) {      // 업데이트 날짜를 기준으로 정렬 (신상순)
            pagingInfo = PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "updatedDate"));
        }

        if (StringUtils.isBlank(goodsName)) {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return CompletableFuture.completedFuture(goodsRepository.findGoodsList(pagingInfo));
            } else {
                return CompletableFuture.completedFuture(goodsRepository.findGoodsListByType(type, pagingInfo));
            }
        } else {
            if (StringUtils.isBlank(type) || type.equals("전체")) {
                return CompletableFuture.completedFuture(goodsRepository.findGoodsListByName(goodsName, pagingInfo));
            } else {
                return CompletableFuture.completedFuture(goodsRepository.findGoodsListByNameAndType(goodsName, type, pagingInfo));
            }
        }
    }

    // 사용자별 추천 리스트
    public List<IGoodsView> getRecommendationGoodsList(String abvLimit, List<String> typeOrLabels,
                                                       UserRecommendationBase.EFigure feeling, List<String> goodsNameList) {
        int defaultPage = 0;
        int defaultSize = 10;
        Pageable pagingInfo = PageRequest.of(defaultPage, defaultSize);

        // opt5Value == sweetness
        if (feeling == UserRecommendationBase.EFigure.HIGH) {
            pagingInfo = PageRequest.of(defaultPage, defaultSize, Sort.by(Sort.Direction.DESC, "opt5Value"));
        } else if (feeling == UserRecommendationBase.EFigure.LOW) {
            pagingInfo = PageRequest.of(defaultPage, defaultSize, Sort.by(Sort.Direction.ASC, "opt5Value"));
        }

        List<IGoodsView> iGoodsViewList;
        String goodsNames = String.join(" ", goodsNameList);

        if (goodsNameList == null || goodsNameList.size() == 0) {
            if (typeOrLabels == null || typeOrLabels.size() == 0) {
                iGoodsViewList = goodsRepository.findRecommendationGoodsList(abvLimit, pagingInfo);
            } else {
                iGoodsViewList = goodsRepository.findRecommendationGoodsList(abvLimit, typeOrLabels, pagingInfo);
            }
        } else {
            if (typeOrLabels == null || typeOrLabels.size() == 0) {
                iGoodsViewList = goodsRepository.findRecommendationGoodsListByGoodsNameList(abvLimit, goodsNames, pagingInfo);
            } else {
                iGoodsViewList = goodsRepository.findRecommendationGoodsListByGoodsNameList(abvLimit, typeOrLabels, goodsNames, pagingInfo);
            }
        }

        return iGoodsViewList;
    }

    public List<IGoodsView> findNewGoodsListDefault() {
        return findNewGoodsList(null, null);
    }

    // 상품(GoodsView) n개 조회 (신상품)
    public List<IGoodsView> findNewGoodsList(Integer page, Integer size) {
        int defaultPage = 0;
        int defaultSize = 10;
        int requestedPage = page != null ? page : defaultPage;
        int requestedSize = size != null ? size : defaultSize;

        return goodsRepository.findNewGoodsList(
                PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "createdDate")));
    }

    // 최종 단일 가격 조회
    public int getSingleSalePrice(String goodsName) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isEmpty()) {
            return -1;
        }

        Goods goods = oneByName.get();
        GoodsDiscount goodsDiscount = goodsDiscountRepository.findByGoods(goods);

        if (goodsDiscount == null) {    // 상품 할인 정보가 없는 경우
            return goods.getPrice() == null ? 0 : goods.getPrice();
        }

        return goods.getPrice() * (int) ((double) (100 - goodsDiscount.getDiscountRate()) / 100);
    }

    // 가격 변경
    @Transactional
    public void changePrice(String goodsName, Integer price) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.updatePrice(price);
        }
    }

    // 재고 변경 (원하는 수치)
    @Transactional
    public void changeStock(String goodsName, Integer stock) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.updateStock(stock);
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

    // 판매량 변경 (원하는 수치)
    @Transactional
    public void changeSalesFigures(String goodsName, Long salesFigures) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.updateSalesFigures(salesFigures);
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
            goods.updateSalesStatus(salesStatus);
        }
    }

    // 상품 1개 추가
    @Transactional
    public void addGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    // 상품 여러개 추가
    @Transactional
    public void addGoodsList(List<Goods> goodsList) {
        goodsRepository.saveAll(goodsList);
    }
}
