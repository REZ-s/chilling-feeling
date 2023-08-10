package com.joolove.core.repository;

import com.joolove.core.domain.billing.Cart;
import com.joolove.core.domain.product.CartGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartGoodsRepository extends JpaRepository<CartGoods, UUID> {

    @Query("select cg " +
            "from CartGoods cg " +
            "where cg.cart.id = ?1 ")
    public List<CartGoods> findByCartId(UUID cartId);
}
