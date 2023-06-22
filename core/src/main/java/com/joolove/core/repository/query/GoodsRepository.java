package com.joolove.core.repository.query;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.dto.query.IGoodsView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GoodsRepository extends JpaRepository<Goods, UUID> {

    Optional<Goods> findOneByName(String name);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id")
    List<IGoodsView> findNewGoodsList(Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id")
    List<IGoodsView> findGoodsList(Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.type = ?1")
    List<IGoodsView> findGoodsListByType(String type, Pageable pageable);

    @Query(value = "select gd.name as name, gd.type as type, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from goods_details gd inner join goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and match(gd.name) against (concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findGoodsListByName(String name, Pageable pageable);

    @Query(value = "select gd.name as name, gd.type as type, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from goods_details gd inner join goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and gd.type = ?2 " +
            "and match(gd.name) against (concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findGoodsListByNameAndType(String name, String type, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsViewDetails(" +
            "gd.name, gd.engName, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount, gd.degree, " +
            "gd.country, gd.company, gd.supplier, gd.color, gd.colorImageUrl, gd.description, gd.descriptionImageUrl, " +
            "gd.summary, gd.opt1Value, gd.opt2Value, gd.opt3Value, gd.opt4Value, gd.opt5Value, gd.opt6Value, gd.opt7Value)  " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.name = ?1 " +
            "and gd.goods.id = gs.goods.id")
    GoodsViewDetails findGoodsDetailByName(String name);


}