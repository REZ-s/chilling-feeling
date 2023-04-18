package com.joolove.core.security.service;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.repository.GoodsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsRepository goodsRepository;

    public List<Goods> findListGoods(String goodsName) {
        return goodsRepository.findListByName(goodsName);
    }

    public Goods findOneByGoodsName(String goodsName) {
        return goodsRepository.findOneByName(goodsName).orElse(null);
    }

    // 가격 변경하는 함수
    @Transactional
    public void changePrice(String goodsName, Integer price) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changePrice(price);
        }
    }

    // 재고 변경하는 함수
    @Transactional
    public void changeStock(String goodsName, Integer stock) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeStock(stock);
        }
    }

    // 재고 하나씩 증가시키는 함수
    @Transactional
    public void increaseStock(String goodsName) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.increaseStock();
        }
    }

    // 판매량 변경하는 함수
    @Transactional
    public void changeSalesFigures(String goodsName, Long salesFigures) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeSalesFigures(salesFigures);
        }
    }

    // 판매량 하나씩 증가시키는 함수
    @Transactional
    public void increaseSalesFigures(String goodsName) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.increaseSalesFigures();
        }
    }

    // 판매 상태 변경하는 함수
    @Transactional
    public void changeSalesStatus(String goodsName, Short salesStatus) {
        Optional<Goods> oneByName = goodsRepository.findOneByName(goodsName);
        if (oneByName.isPresent()) {
            Goods goods = oneByName.get();
            goods.changeSalesStatus(salesStatus);
        }
    }

    @Transactional
    public void addGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Transactional
    public void addGoodsList(List<Goods> goodsList) {
        goodsRepository.saveAll(goodsList);
    }
}
