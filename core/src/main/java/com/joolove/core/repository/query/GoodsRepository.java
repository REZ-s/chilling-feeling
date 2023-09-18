package com.joolove.core.repository.query;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.dto.query.GoodsViewDetails;
import com.joolove.core.dto.query.IGoodsView;
import com.joolove.core.dto.query.IGoodsViewDetails;
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

    @Query("select g " +
            "from Goods g " +
            "where g.name in ?1")
    List<Goods> findAllByNameIn(List<String> goodsNameList);

    @Query(value = "select count(*) " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and match(gd.name) against(concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    long getGoodsListCountByName(String name);

    @Query("select count(*) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.type = ?1")
    long getGoodsListCountByType(String type);

    @Query(value = "select count(*) " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and gd.type = ?2 " +
            "and match(gd.name) against(concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    long getGoodsListCountByNameAndType(String name, String type);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.name = ?1 ")
    IGoodsView findGoodsByGoodsName(String goodsName);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.name in ?1 ")
    List<IGoodsView> findGoodsListByGoodsName(List<String> goodsNameList);

    // 문자열의 길이가 길 때, FTS 가 생각보다 느리다면 기존의 like, in 도 고려해본다. (성능 테스트 필요)
    @Query(value = "select gd.name as name, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and (gd.type in ?2 or gd.sub_type in ?2 or gs.label in ?2) " +
            "and gd.degree <= ?1 " +
            "and match(gd.name) against(concat('*', ?3, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findRecommendationGoodsListByGoodsNameList(String abvLimit, List<String> typeOrLabels, String goodsNames, Pageable pagingInfo);

    @Query(value = "select gd.name as name, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and gd.degree <= ?1 " +
            "and match(gd.name) against(concat('*', ?2, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findRecommendationGoodsListByGoodsNameList(String abvLimit, String goodsNames, Pageable pagingInfo);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and (gd.type in ?2 or gd.subType in ?2 or gs.label in ?2) " +
            "and gd.degree <= ?1")
    List<IGoodsView> findRecommendationGoodsList(String abvLimit, List<String> typeOrLabels, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.degree <= ?1")
    List<IGoodsView> findRecommendationGoodsList(String abvLimit, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id")
    List<IGoodsView> findNewGoodsList(Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id")
    List<IGoodsView> findGoodsList(Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(" +
            "gd.name, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.goods.id = gs.goods.id " +
            "and gd.type = ?1")
    List<IGoodsView> findGoodsListByType(String type, Pageable pageable);

    @Query(value = "select gd.name as name, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and match(gd.name) against(concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findGoodsListByName(String name, Pageable pageable);

    @Query(value = "select gd.name as name, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, gs.label as label, gs.score as score, gs.review_count as reviewCount " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "and gd.type = ?2 " +
            "and match(gd.name) against(concat('*', ?1, '*') in boolean mode) ", nativeQuery = true)
    List<IGoodsView> findGoodsListByNameAndType(String name, String type, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsViewDetails(" +
            "gd.name, gd.engName, gd.type, gd.subType, gd.imageUrl, gs.label, gs.score, gs.reviewCount, gd.degree, " +
            "gd.country, gd.company, gd.supplier, gd.color, gd.colorImageUrl, gd.description, gd.descriptionImageUrl, " +
            "gd.summary, gd.opt1Value, gd.opt2Value, gd.opt3Value, gd.opt4Value, gd.opt5Value, gd.opt6Value, gd.opt7Value)  " +
            "from GoodsDetails gd " +
            "inner join GoodsStats gs " +
            "on gd.name = ?1 " +
            "and gd.goods.id = gs.goods.id")
    GoodsViewDetails findGoodsDetailsByName(String name);

    @Query(value = "select gd.name as name, gd.eng_name as engName, gd.type as type, gd.sub_type as subType, gd.image_url as imageUrl, " +
            "gs.label as label, gs.score as score, gs.review_count as reviewCount, gd.degree as degree, " +
            "gd.country as country, gd.company as company, gd.supplier as supplier, gd.color as color, gd.color_image_url as colorImageUrl, " +
            "gd.description as description, gd.description_image_url as descriptionImageUrl, gd.summary as summary, " +
            "gd.opt1value as opt1Value, gd.opt2value as opt2Value, gd.opt3value as opt3Value, gd.opt4value as opt4Value, " +
            "gd.opt5value as opt5Value, gd.opt6value as opt6Value, gd.opt7value as opt7Value " +
            "from product.goods_details gd " +
            "inner join product.goods_stats gs " +
            "on gd.goods_id = gs.goods_id " +
            "inner join ( " +
            "    select goods_id " +
            "    from product.goods_stats " +
            "    order by rand() " +
            "    limit 1 " +
            ") as sgs " +
            "on gd.goods_id = sgs.goods_id ", nativeQuery = true)
    List<IGoodsViewDetails> findGoodsDetailsRandom();
}