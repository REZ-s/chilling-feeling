package com.joolove.core.repository;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.domain.product.GoodsDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsDiscountRepository extends JpaRepository<GoodsDiscount, UUID> {

    GoodsDiscount findByGoods(Goods goods);
}
