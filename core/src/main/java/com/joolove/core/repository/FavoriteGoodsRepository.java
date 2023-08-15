package com.joolove.core.repository;

import com.joolove.core.domain.product.CartGoods;
import com.joolove.core.domain.product.FavoriteGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteGoodsRepository extends JpaRepository<FavoriteGoods, UUID> {

    @Query("select fg " +
            "from FavoriteGoods fg " +
            "where fg.favorite.id = ?1 ")
    public List<FavoriteGoods> findByFavoriteId(UUID favoriteId);
}
