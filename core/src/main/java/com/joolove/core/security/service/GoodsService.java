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

}
