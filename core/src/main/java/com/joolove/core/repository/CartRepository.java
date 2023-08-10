package com.joolove.core.repository;

import com.joolove.core.domain.billing.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {

    @Query("select c " +
            "from Cart c " +
            "where c.user.id = ?1 ")
    public Cart findByUserId(UUID userId);
}
