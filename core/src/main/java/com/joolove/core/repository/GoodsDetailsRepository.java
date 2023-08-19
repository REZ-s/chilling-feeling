package com.joolove.core.repository;

import com.joolove.core.domain.product.GoodsDetails;
import com.joolove.core.dto.query.GoodsView;
import com.joolove.core.dto.query.GoodsViewDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GoodsDetailsRepository extends JpaRepository<GoodsDetails, UUID> {

    @Query("select new com.joolove.core.dto.query.GoodsViewDetails(" +
            "gd.name, gd.engName, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount, gd.degree, " +
            "gd.country, gd.company, gd.supplier, gd.color, gd.colorImageUrl, gd.description, gd.descriptionImageUrl, " +
            "gd.summary, gd.opt1Value, gd.opt2Value, gd.opt3Value, gd.opt4Value, gd.opt5Value, gd.opt6Value, gd.opt7Value) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "inner join Goods g " +
            "on g.id = ?1 " +
            "and gs.goods.id = g.id ")
    GoodsViewDetails findGoodsByGoodsId(UUID goodsId);

}
