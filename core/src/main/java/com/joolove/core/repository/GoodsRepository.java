package com.joolove.core.repository;

import com.joolove.core.domain.product.Goods;
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

    @Query("select gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount " +
            "from GoodsDetails gd, GoodsStats gs")
    List<Goods.GoodsView> findGoodsList(Pageable pageable);

    @Query("select gd.name as name, gd.type as type, gd.imageUrl as imageUrl, gs.label as label, gs.score as score, gs.reviewCount as reviewCount " +
            "from GoodsDetails as gd, GoodsStats as gs " +
            "where gd.name like %?1% or gd.engName like %?1%")
    List<Goods.GoodsView> findGoodsListByName(String name, Pageable pageable);

    @Query("select gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount " +
            "from GoodsDetails gd, GoodsStats gs " +
            "where gd.type = ?1")
    List<Goods.GoodsView> findGoodsListByType(String type, Pageable pageable);

    @Query("select gd.name, gd.type, gd.imageUrl, gs.label, gs.score, gs.reviewCount " +
            "from GoodsDetails gd, GoodsStats gs " +
            "where (gd.name like %?1% or gd.engName like %?1%) " +
            "and gd.type = ?2")
    List<Goods.GoodsView> findGoodsListByNameAndType(String name, String type, Pageable pageable);

    @Query("select gd " +
            "from GoodsDetails gd " +
            "where gd.name = ?1")
    Goods.GoodsViewDetail findGoodsDetailByName(String name);
}