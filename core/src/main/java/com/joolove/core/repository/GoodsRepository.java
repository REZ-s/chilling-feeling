package com.joolove.core.repository;

import com.joolove.core.domain.product.Goods;
import com.joolove.core.dto.query.GoodsView;
import com.joolove.core.dto.query.GoodsViewDetails;
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

    @Query("select new com.joolove.core.dto.query.GoodsView(gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd, GoodsStats gs")
        List<GoodsView> findGoodsList(Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails as gd, GoodsStats as gs " +
            "where gd.name like %?1% or gd.engName like %?1%")
    List<GoodsView> findGoodsListByName(String name, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd, GoodsStats gs " +
            "where gd.type = ?1")
    List<GoodsView> findGoodsListByType(String type, Pageable pageable);

    @Query("select new com.joolove.core.dto.query.GoodsView(gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount) " +
            "from GoodsDetails gd, GoodsStats gs " +
            "where (gd.name like %?1% or gd.engName like %?1%) " +
            "and gd.type = ?2")
    List<GoodsView> findGoodsListByNameAndType(String name, String type, Pageable pageable);

/*    @Query(value = "select new com.joolove.core.dto.query.GoodsViewDetails(" +
            "gd.name, gd.engName, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount, " +
            "gd.degree, gd.country, gd.company, gd.supplier, gd.color, gd.colorImageUrl, gd.description, gd.descriptionImageUrl, " +
            "gd.summary, gd.opt1Value, gd.opt2Value, gd.opt3Value, gd.opt4Value, gd.opt5Value, gd.opt6Value, gd.opt7Value)  " +
            "from GoodsDetails gd inner join GoodsStats gs " +
            "where match(gd.name) against(?1 in boolean mode)" +
            "and gd.goods.id = gs.goods.id", nativeQuery = true)*/
    @Query(value = "select gd.name, gd.eng_name, gd.type, gd.image_url, gs.label, gs.score, gs.review_count" +
        "gd.degree, gd.country, gd.company, gd.supplier, gd.color, gd.color_image_url, gd.description, gd.description_image_url" +
        "gd.summary, gd.opt1value, gd.opt2value, gd.opt3value, gd.opt4value, gd.opt5value, gd.opt6value, gd.opt7value" +
        "from cfdb1.goods_details as gd inner join cfdb1.goods_stats as gs" +
        "where match(gd.name) against(?1 in boolean mode)" +
        "and gd.goods_id = gs.goods_id", nativeQuery = true)
    GoodsViewDetails findGoodsDetailByName(String name);

    // 이렇게 할 필요가 없네? 생각해보니 상품 상세 페이지는 이미 어떤 상품 1개를 정확하게 알고 있는 상황이고
    // 상품 이름을 정확하게 일치하는 것만 찾으면 되는 것이니까 ... name 에

}