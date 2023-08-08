package com.joolove.core.repository;

import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.dto.query.GoodsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsDetailsRepository extends JpaRepository<GoodsDetails, UUID> {

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "inner join Goods g " +
            "on g.id = ?1")
    GoodsView findGoodsByGoodsId(UUID goodsId);

}
