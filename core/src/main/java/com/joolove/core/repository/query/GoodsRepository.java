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

    // 문자열의 길이가 길 때, FTS 가 생각보다 느리다면 기존의 like in 도 고려해본다. (성능 테스트 필요)
    // 와인 > 레드와인 의 경우처럼, type 외에 sub_type 을 만들어줄까 생각중..
    @Query(value = "select gd.name as name, gd.type as type, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from goods_details gd inner join goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and (gd.type in (?2) or gs.label = ?2) " +
            "and gd.degree <= ?1 " +
            "and match(gd.name) against (concat('*', ?3, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findRecommendationGoodsListByGoodsNameList(String abvLimit, String typeOrLabels, String goodsNames, Pageable pagingInfo);

    @Query(value = "select gd.name as name, gd.type as type, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from goods_details gd inner join goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and gd.degree <= ?1 " +
            "and match(gd.name) against (concat('*', ?2, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findRecommendationGoodsListByGoodsNameList(String abvLimit, String goodsNames, Pageable pagingInfo);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and (gd.type in (?2) or gs.label = ?2) " +
            "and gd.degree <= ?1")
    List<IGoodsView> findRecommendationGoodsList(String abvLimit, String typeOrLabels, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.degree <= ?1")
    List<IGoodsView> findRecommendationGoodsList(String abvLimit, Pageable pageable);

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