package com.joolove.core.service;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.repository.GoodsDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsDetailsService {

    private final GoodsDetailsRepository goodsDetailsRepository;

    @Transactional
    public void addGoodsDetails(GoodsDetails goodsDetails) {
        goodsDetailsRepository.save(goodsDetails);
    }
}
