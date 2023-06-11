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

    @Query("select new com.joolove.core.dto.query.GoodsViewDetails(" +
            "gd.name, gd.engName, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount, " +
            "gd.degree, gd.country, gd.company, gd.supplier, gd.color, gd.colorImageUrl, gd.description, gd.descriptionImageUrl, " +
            "gd.summary, gd.opt1Value, gd.opt2Value, gd.opt3Value, gd.opt4Value, gd.opt5Value, gd.opt6Value, gd.opt7Value)  " +
            "from GoodsDetails gd, GoodsStats gs " +
            "where gd.name = ?1")
    GoodsViewDetails findGoodsDetailByName(String name);


}